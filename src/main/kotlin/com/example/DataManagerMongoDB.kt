package com.example

import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters.eq
import org.bson.Document
import org.bson.codecs.configuration.CodecRegistries.fromProviders
import org.bson.codecs.configuration.CodecRegistries.fromRegistries
import org.bson.codecs.configuration.CodecRegistry
import org.bson.codecs.pojo.PojoCodecProvider
import org.bson.types.ObjectId
import org.slf4j.LoggerFactory

class DataManagerMongoDB {

  private val log = LoggerFactory.getLogger(DataManagerMongoDB::class.java)

  private val database: MongoDatabase
  private val bookCollection: MongoCollection<Book>

  companion object{
    private val ID = "_id"
  }
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
    bookCollection = database.getCollection(Book::class.java.name, Book::class.java)
    initBooks()
  }

  private fun initBooks() {
    bookCollection.insertOne(Book(null, "How to grow apples", "Mr. Appleton", 100.0f))
    bookCollection.insertOne(Book(null, "How to grow oranges", "Mr. Oranges", 90.0f))
    bookCollection.insertOne(Book(null, "How to grow lemons", "Mr. Lemons", 110.0f))
    bookCollection.insertOne(Book(null, "How to grow pineapples", "Mr. Pineapple", 100.0f))
    bookCollection.insertOne(Book(null, "How to grow pears", "Mr. Pears", 110.0f))
    bookCollection.insertOne(Book(null, "How to grow coconuts", "Mr. Coconuts", 130.0f))
    bookCollection.insertOne(Book(null, "How to grow bananas", "Mr. Appleton", 120.0f))
  }

  fun newBook(book: Book): Book {
    bookCollection.insertOne(book)
    return book
  }

  fun updateBook(book: Book): Book? {
    return bookCollection.find(Document(ID, book.id)).first()?.apply {
      title = book.title
      author = book.author
      price = book.price
    }
  }

  fun deleteBook(bookId: String): Book? {
    val foundBook = bookCollection.find(Document(ID, bookId)).first()
    bookCollection.deleteOne(eq(ID, ObjectId(bookId)))
    return foundBook
  }

  fun allBooks(): List<Book> {
    return bookCollection.find().toList()
  }

  fun sortedBooks(sortBy: String, asc: Boolean): List<Book> {
    val pageNr = 1
    val pageSize = 1000
    val ascInt: Int = if (asc) 1 else -1

    return bookCollection.find()
      .sort(Document(mapOf(sortBy to ascInt, ID to -1)))
      .skip(pageNr - 1)
      .limit(pageSize)
      .toList()
  }
}