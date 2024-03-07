package com.example.ui.data

import com.example.routes.Session
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters.empty
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Filters.or
import com.mongodb.client.model.Filters.regex
import kotlinx.coroutines.runBlocking
import org.bson.Document
import org.bson.codecs.configuration.CodecRegistries.fromProviders
import org.bson.codecs.configuration.CodecRegistries.fromRegistries
import org.bson.codecs.configuration.CodecRegistry
import org.bson.codecs.pojo.PojoCodecProvider
import org.bson.types.ObjectId
import org.slf4j.LoggerFactory

object DataManagerMongoDB {

  private val database: MongoDatabase
  private val bookCollection: MongoCollection<MongoBook>
  private val cartCollection: MongoCollection<Cart>

  private const val ID = "_id"
  private val log = LoggerFactory.getLogger(DataManagerMongoDB.javaClass)

  init {
    val pojoCodecRegistry: CodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build())
    val codecRegistry: CodecRegistry = fromRegistries(
      MongoClientSettings.getDefaultCodecRegistry(),
      pojoCodecRegistry
    )

    val clientSettings = MongoClientSettings.builder()
      .codecRegistry(codecRegistry)
      .build()

    val mongoClient = MongoClients.create(clientSettings)
    database = mongoClient.getDatabase("development");
    bookCollection = database.getCollection(MongoBook::class.java.name, MongoBook::class.java)
    cartCollection = database.getCollection(Cart::class.java.name, Cart::class.java)
    runBlocking {
      bookCollection.deleteMany(empty())
      cartCollection.deleteMany(empty())
      newBook(Book("How to grow apples", "Mr. Appleton", 100f))
      newBook(Book("How to grow oranges", "Mr. Orangeton", 90f))
      newBook(Book("How to grow lemons", "Mr. Lemon", 110f))
      newBook(Book("How to grow pineapples", "Mr. Pineapple", 100f))
      newBook(Book("How to grow pears", "Mr. Pears", 120f))
      newBook(Book("How to grow coconuts", "Mr. Coconut", 130f))
      newBook(Book("How to grow bananas", "Mr. Appleton", 120f))
    }
  }

  fun newBook(book: Book): Book {
    bookCollection.insertOne(book.toMongo())
    return book
  }

  fun updateBook(book: Book): Book? {
    return bookCollection.find(Document(ID, book.id)).first()?.apply {
      title = book.title
      author = book.author
      price = book.price
    }?.toBook()
  }

  fun deleteBook(bookId: String): Book? {
    val foundBook = bookCollection.find(Document(ID, bookId)).first()
    bookCollection.deleteOne(eq(ID, ObjectId(bookId)))
    return foundBook?.toBook()
  }

  fun allBooks(): List<Book> {
    return bookCollection.find().map{ it.toBook() }.toList()
  }

  fun sortedBooks(sortBy: String, asc: Boolean): List<Book> {
    val pageNr = 1
    val pageSize = 1000
    val ascInt: Int = if (asc) 1 else -1

    return bookCollection.find()
      .sort(Document(mapOf(sortBy to ascInt, ID to -1)))
      .skip(pageNr - 1)
      .limit(pageSize)
      .map { it.toBook() }
      .toList()
  }

  fun searchBooks(input: String): List<Book> {
    return bookCollection.find(
      or(
        regex("title", ".*$input.*"),
        regex("author", ".*$input.*")
      ))
      .sort(Document(mapOf("title" to 1, "_id" to -1)))
      .map { it.toBook() }
      .toList()
  }

  fun updateCart(cart: Cart?) {
    cart?.let {
      val replaceOne = cartCollection.replaceOne(eq("username", cart.username), cart)
      log.info("Update result: $replaceOne")
    }
  }

  fun addBook(session: Session, book: MongoBook) {
    val cartForUser = cartForUser(session)
    cartForUser?.addBook(book)
    updateCart(cartForUser)
  }

  fun cartForUser(session: Session): Cart? {
    val find = cartCollection.find(eq("username", session.username)).toList()
    return if (find.isEmpty()) {
      val cart = Cart(session.username)
      cartCollection.insertOne(cart).insertedId.let {
        cartCollection.find(eq("_id", it)).first()
      }
    } else find.first()
  }

  fun getBookWithId(bookId: String): MongoBook? {
    log.info("Get book with id: $bookId")
    return bookCollection.find(eq("_id", ObjectId(bookId))).first()
  }

  fun removeBook(session: Session, book: MongoBook) {
    val cartForUser = cartForUser(session)
    cartForUser?.removeBook(book)
    updateCart(cartForUser)
  }
}