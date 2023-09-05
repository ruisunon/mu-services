package com.rean.service.impl;

import com.rean.dto.BookRequestDto;
import com.rean.dto.BookResponseDto;
import com.rean.model.*;
import com.rean.repository.BookRepository;
import com.rean.repository.PageClazzRepository;
import com.rean.service.AuthorService;
import com.rean.service.BookService;
import com.rean.service.CategoryService;
import com.rean.service.PhotoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final ModelMapper modelMapper;
    private final AuthorService authorService;
    private final BookRepository bookRepository;
    private final PhotoService photoService;
    private final CategoryService categoryService;
    private final PageClazzRepository pageClazzRepository;

    @Override
    public void createNewBook(BookRequestDto requestDto) throws Exception {

        Author author = authorService.findById(requestDto.getAuthorId());
        if(Objects.isNull(author)) {
            log.error("Author {} not found.", requestDto.getPhotoId());
            throw new Exception();
        }
        Photo photo = photoService.checkById(requestDto.getPhotoId());
        if(Objects.isNull(photo)) {
            log.error("PHOTO-ID {} not found.", requestDto.getPhotoId());
        }

        Category category = categoryService.checkById(requestDto.getCategoryId());
        if(Objects.isNull(category)) {
            log.error("CATEGORY-ID {} not found.", requestDto.getCategoryId());
            throw new Exception();
        }
        PageClazz pageClazz = modelMapper.map(requestDto.getPageDto(), PageClazz.class);
        Book book = new Book();
        book.setTitle(requestDto.getTitle());
        book.setIsbn(requestDto.getIsbn());
        book.setPublishedDate(requestDto.getPublishedDate());
        book.setRating(requestDto.getRating());
        book.setAuthors(Collections.singletonList(author));
        book.setCategories(Collections.singletonList(category));
        book.setPhoto(photo);
        bookRepository.save(book);
        pageClazz.setBook(book);
        pageClazzRepository.save(pageClazz);
    }

    @Override
    public List<BookResponseDto> listBooks() {
        return bookRepository.findAll().stream()
                .map(book -> modelMapper.map(book, BookResponseDto.class))
                .collect(Collectors.toList());
    }

}
