class Book {
    constructor(
        title = 'Unknown',
        author = 'Unknown',
        pages = '0',
        isRead = false
      ) {
        this.title = title
        this.author = author
        this.pages = pages
        this.isRead = isRead
      }
}

class Library {
    constructor() {
        this.books = [];
    }

    addBook(newBook) {
        if (!this.isInLibrary(newBook)) {
            this.books.push(newBook);
        }
    }

    removeBook(title) {
        this.books = this.books.filter((book) => book.title !== title);
    }

    getBook(title) {
        return this.books.find((book) => book.title === title);
    }

    isInLibrary(newBook) {
        return this.books.some((book) => book.title === newBook.title);
    }
}

const library = new Library();

let modal = document.querySelector('.modal');
let addBtn = document.querySelector('.add-btn');

let newBookTitle = document.querySelector("#title");
let newBookAuthor = document.querySelector("#author");
let newBookPages = document.querySelector("#pages");
let newBookIsRead = document.querySelector("#read");
let newBookSubmitBtn = document.querySelector("#submit-btn");
let addBookForm = document.querySelector(".add-book-form");

let removeBtn = document.querySelectorAll(".remove-btn");
let readBtn = document.querySelectorAll(".read-btn");

let booksGrid = document.querySelector(".books-grid");

const addBook = (e) => {
    e.preventDefault()
    const newBook = getBookFromInput()
  
    library.addBook(newBook)

    updateBooksGrid()
  
    closeAddBookModal()
  }

  const removeBook = (e) => {
    const title = e.target.parentNode.parentNode.firstChild.innerHTML.replaceAll(
      '"',
      ''
    )
      library.removeBook(title)
      updateBooksGrid()
  }

  const toggleRead = (e) => {
    const title = e.target.parentNode.parentNode.firstChild.innerHTML.replaceAll(
      '"',
      ''
    )
    const book = library.getBook(title)
  
   
      book.isRead = !book.isRead
      updateBooksGrid()
    
  }

addBtn.addEventListener('click', () => {
    openAddBookModal();
})

newBookSubmitBtn.addEventListener('click', () => {
    library.addBook(getBookFromInput);
    updateBooksGrid();
})

addBookForm.addEventListener('submit', addBook)

const openAddBookModal = () => {
    addBookForm.reset()
    modal.classList.add('active')
    overlay.classList.add('active')
}

const getBookFromInput = () => {
    let newTitle = newBookTitle.value;
    let newAuthor = newBookAuthor.value;
    let newPages = newBookPages.value;
    let newIsRead = newBookIsRead.checked;
    return new Book(newTitle, newAuthor, newPages, newIsRead);
}

const updateBooksGrid = () => {
  resetBooksGrid()
  for (let book of library.books) {
    if (book.title != undefined)
        createBookCard(book)
  }
}

const resetBooksGrid = () => {
  booksGrid.innerHTML = ''
}

const createBookCard = (book) => {
    const bookCard = document.createElement('div')
    const title = document.createElement('p')
    const author = document.createElement('p')
    const pages = document.createElement('p')
    const buttonGroup = document.createElement('div')
    const readBtn = document.createElement('button')
    const removeBtn = document.createElement('button')
  
    bookCard.classList.add('book-card')
    buttonGroup.classList.add('btn-group')
    readBtn.classList.add('btn')
    readBtn.classList.add('read-btn')
    removeBtn.classList.add('btn')
    removeBtn.classList.add('remove-btn')
    readBtn.onclick = toggleRead
    removeBtn.onclick = removeBook
  
    title.textContent = `"${book.title}"`
    author.textContent = book.author
    pages.textContent = `${book.pages} pages`
    removeBtn.textContent = 'Remove'
  
    if (book.isRead) {
      readBtn.textContent = 'Read'
      readBtn.classList.add('btn-green')
    } else {
      readBtn.textContent = 'Not read'
      readBtn.classList.add('btn-red')
    }
  
    bookCard.appendChild(title)
    bookCard.appendChild(author)
    bookCard.appendChild(pages)
    buttonGroup.appendChild(readBtn)
    buttonGroup.appendChild(removeBtn)
    bookCard.appendChild(buttonGroup)
    booksGrid.appendChild(bookCard)
  }

  const closeAddBookModal = () => {
    modal.classList.remove('active')
    overlay.classList.remove('active')
  }

  overlay.onclick = closeAddBookModal





