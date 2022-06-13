package com.piisw.UrbanTicketSystem;

import com.piisw.UrbanTicketSystem.domain.model.TicketCategory;
import com.piisw.UrbanTicketSystem.domain.model.User;
import com.piisw.UrbanTicketSystem.infrastructure.jpa.adapter.JpaTicketCategoryService;
import com.piisw.UrbanTicketSystem.infrastructure.jpa.adapter.JpaUserService;
import com.piisw.UrbanTicketSystem.infrastructure.jpa.model.TicketCategoryEntity;
import com.piisw.UrbanTicketSystem.infrastructure.jpa.model.UserEntity;
import com.piisw.UrbanTicketSystem.infrastructure.jpa.repository.JpaTicketCategoryRepository;
import com.piisw.UrbanTicketSystem.infrastructure.jpa.repository.JpaUserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = {JpaTicketCategoryService.class})
public class TicketCategoryServiceTests {
    @InjectMocks
    JpaTicketCategoryService jpaTicketCategoryService;

    @Mock
    JpaTicketCategoryRepository jpaTicketCategoryRepository;

    @Test
    public void shouldReturnAllTicketCategories() {
        TicketCategoryEntity ticketCategory1 = new TicketCategoryEntity(0L, "Category1");
        TicketCategoryEntity ticketCategory2 = new TicketCategoryEntity(1L, "Category2");
        List<TicketCategoryEntity> ticketCategoriesEntitiesList = new ArrayList<TicketCategoryEntity>();
        ticketCategoriesEntitiesList.add(ticketCategory1);
        ticketCategoriesEntitiesList.add(ticketCategory2);

        doReturn(ticketCategoriesEntitiesList).when(jpaTicketCategoryRepository).findAll();
        List<TicketCategory> returnedCategories = jpaTicketCategoryService.getAll();
        assertThat(returnedCategories.get(0).getName()).isSameAs(ticketCategory1.getName());
        assertThat(returnedCategories.get(1).getName()).isSameAs(ticketCategory2.getName());
    }
}
