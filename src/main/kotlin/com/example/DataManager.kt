package com.example

class DataManager {

  private var books = ArrayList<Book>()

  private fun generateId(): String {
    return books.size.toString()
  }

  init {
    books.add(Book(generateId(), "How to grow apples", "Mr. Appleton", 100.0f))
    books.add(Book(generateId(), "How to grow oranges", "Mr. Oranges", 90.0f))
    books.add(Book(generateId(), "How to grow lemons", "Mr. Lemons", 110.0f))
    books.add(Book(generateId(), "How to grow pineapples", "Mr. Pineapple", 100.0f))
    books.add(Book(generateId(), "How to grow pears", "Mr. Pears", 110.0f))
    books.add(Book(generateId(), "How to grow coconuts", "Mr. Coconuts", 130.0f))
    books.add(Book(generateId(), "How to grow bananas", "Mr. Appleton", 120.0f))
  }

  fun newBook(book: Book): Book {
    books.add(book)
    return book
  }

  fun updateBook(book: Book): Book? {
    return books.find { it.id == book.id }?.apply {
      title = book.title
      author = book.author
      price = book.price
    }
  }

  fun deleteBook(bookId: String): Book? {
    val foundBook = books.find { it.id == bookId }
    books.remove(foundBook)
    return foundBook
  }

  fun allBooks(): List<Book> {
    return books
  }
}