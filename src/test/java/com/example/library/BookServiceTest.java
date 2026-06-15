package com.example.library;

import com.example.library.entity.Book;
import com.example.library.exception.BookNotFoundException;
import com.example.library.repository.BookRepository;
import com.example.library.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    void getAllBooks_returnList() {
        // 1. Tạo 2 Book giả
        List<Book> mockBooks = List.of(
                new Book(1L, "Book 1", "Author 1", "info", 100),
                new Book(2L, "Book 2", "Author 2", "info", 101)
        );

        // 2. Mock: khi gọi repository.findAll() thì trả về danh sách giả
        when(bookRepository.findAll()).thenReturn(mockBooks);

        // 3. Gọi service
        List<Book> result = bookService.getAllBooks();

        // 4. Assert
        assertEquals(2, result.size());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void getBookById_found() {
        // 1. Tạo 1 Book giả
        Book mockBook = new Book(1L, "Java Basics", "Author A", "info", 100);

        // 2. Mock: thiết lập hành vi findById
        when(bookRepository.findById(1L)).thenReturn(Optional.of(mockBook));

        // 3. Gọi service
        Book result = bookService.getBookById(1L);

        // 4. Assert
        assertNotNull(result);
        assertEquals("Java Basics", result.getTitle());
    }

    @Test
    void getBookById_notFound() {
        // 1. Mock: trả về Optional rỗng
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        // 2. Assert: kiểm tra xem có ném ra đúng Exception hay không
        assertThrows(BookNotFoundException.class, () -> {
            bookService.getBookById(99L);
        });
    }
}
