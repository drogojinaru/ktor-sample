package com.example.ui.data

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class MongoBook(@BsonId var id: ObjectId = ObjectId(), var title: String, var author: String, var price: Float) {
  constructor(title: String, author: String, price: Float) : this(ObjectId(), title, author, price)

  fun toBook(): Book {
    return Book(title, author, price, id.toHexString())
  }
}

data class Book(var title: String, var author: String, var price: Float, var id: String? = null) {

  fun toMongo(): MongoBook {
    id?.let {
      return MongoBook(if (ObjectId.isValid(this.id)) ObjectId(this.id) else ObjectId(), title, author, price)
    }
    return MongoBook(title, author, price)
  }
}

data class CartEntry(val book: MongoBook, var qty: Int, var sum: Float) {
  constructor(book: MongoBook) : this(book, 1, book.price)
}

data class Cart(
  @BsonId val id: ObjectId, val username: String, var qtyTotal: Int, var sum: Float,
  val entries: MutableList<CartEntry> = ArrayList()
) {
  constructor(username: String) : this(ObjectId(), username, 0, 0f)

  fun addBook(book: MongoBook) {
    entries.find { it.book.id == book.id }?.let {
      it.qty += 1
      it.sum += book.price
    } ?: entries.add(CartEntry(book))

    this.qtyTotal += 1
    this.sum += book.price
  }

  fun removeBook(book: MongoBook) {
    entries.find { it.book.id == book.id }?.let {
      it.qty -= 1
      it.sum -= book.price
      if (it.qty <= 0) entries.remove(it)
      this.qtyTotal -= 1
      this.sum -= book.price
    } ?: return
  }
}

data class ShoppingCart(var id: String, var userId: String, val items: ArrayList<ShoppingItem>)
data class ShoppingItem(var bookId: String, var qty: Int)
data class User(var id: String, var name: String, var username: String, var password: String)