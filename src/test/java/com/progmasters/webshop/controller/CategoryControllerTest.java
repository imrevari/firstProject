package com.progmasters.webshop.controller;


import com.progmasters.webshop.config.SpringWebConfig;
import com.progmasters.webshop.domain.Category;
import com.progmasters.webshop.service.CategoryService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {SpringWebConfig.class, CategoryControllerTest.TestConfiguration.class})
public class CategoryControllerTest {


    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    CategoryService categoryServiceMock;


    @Before
    public void setup() {
//        MockitoAnnotations.initMocks(this);
        Mockito.reset(categoryServiceMock);

        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @After
    public void validate() {
//        validateMockitoUsage();
    }


    @Test
    public void testgetCategoryNames() throws Exception {
        Category category1 = new Category();
        category1.setName("Kaja");


        Category category2 = new Category();
        category2.setName("Ital");


        List<String> categories = new ArrayList<>();
        categories.add(category1.getName());
        categories.add(category2.getName());


        when(categoryServiceMock.findAllName()).thenReturn(categories);

        this.mockMvc.perform(get("/api/categories/names"))
                .andExpect(status().isOk());
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(jsonPath("$", hasSize(2)));
//                .andExpect(jsonPath("$[0].name", is("Kaja")))
//
//                .andExpect(jsonPath("$[1].name", is("Ital")));


        verify(categoryServiceMock).findAllName();
//        verifyNoMoreInteractions(orcServiceMock);
    }











    @Configuration
    static class TestConfiguration {

        @Bean
        public CategoryService categoryService() {
            return Mockito.mock(CategoryService.class);
        }
    }


}
