package com.progmasters.webshop.service;

import com.progmasters.webshop.domain.Category;
import com.progmasters.webshop.repository.CategoryRepository;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class CategoryServiceTest {

    private CategoryService categoryService;

    private CategoryRepository categoryRepositoryMock;


    @Before
    public void setUp() {
        categoryRepositoryMock = mock(CategoryRepository.class);
        categoryService = new CategoryService(categoryRepositoryMock);
    }


    @Test
    public void testSavingCategory(){
        Category category = new Category();

        category.setName("Alma");

        when(categoryRepositoryMock.save(any(Category.class))).thenAnswer(returnsFirstArg());

        Category savedCategory = categoryService.save(category);

        assertEquals(category.getName(), savedCategory.getName());

        verify(categoryRepositoryMock, times(1)).save(any(Category.class));
        verifyNoMoreInteractions(categoryRepositoryMock);

    }

//    @Test
//    public void testFindByName(){
//
//        Category category = new Category();
//
//        category.setName("Alma");
//
////        when(categoryRepositoryMock.save(any(Category.class))).thenAnswer(returnsFirstArg());
//
//        categoryService.save(category);
//
//        Category foundCategory = categoryService.findByName("Alma");
//
//        System.err.println(foundCategory.getName());
//
//        assertEquals(category.getName(), foundCategory.getName());
//
//
//        verify(categoryRepositoryMock, times(1)).save(any(Category.class));
//        verifyNoMoreInteractions(categoryRepositoryMock);
//    }




    }
