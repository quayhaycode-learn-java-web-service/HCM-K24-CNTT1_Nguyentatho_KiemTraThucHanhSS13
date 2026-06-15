package com.example.library;

import com.example.library.controller.BookController;
import com.example.library.entity.Book;
import com.example.library.exception.BookNotFoundException;
import com.example.library.exception.GlobalExceptionHandler;
import com.example.library.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {BookController.class, GlobalExceptionHandler.class})
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAll_shouldReturn200AndList() throws Exception {
        // 1. Mock service trả về danh sách 2 cuốn sách
        List<Book> books = List.of(
                new Book(1L, "Book 1", "Author 1", "info", 100),
                new Book(2L, "Book 2", "Author 2", "info", 101)
        );
        when(bookService.getAllBooks()).thenReturn(books);

        // 2, 3, 4. Thực hiện request và kiểm tra
        mockMvc.perform(get("/api/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Book 1"));
    }

    @Test
    void getById_found_shouldReturn200() throws Exception {
        // 1. Mock service trả về 1 cuốn sách
        Book book = new Book(1L, "Java Basics", "Author A", "info", 100);
        when(bookService.getBookById(1L)).thenReturn(book);

        // 2, 3, 4. Thực hiện request và kiểm tra
        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Java Basics"));
    }

    @Test
    void getById_notFound_shouldReturn404() throws Exception {
        // 1. Mock service ném ra ngoại lệ
        when(bookService.getBookById(99L)).thenThrow(new BookNotFoundException(99L));

        // 2, 3. Thực hiện request và kiểm tra status 404
        mockMvc.perform(get("/api/books/99"))
                .andExpect(status().isNotFound());
    }
}
