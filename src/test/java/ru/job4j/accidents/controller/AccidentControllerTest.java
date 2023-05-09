package ru.job4j.accidents.controller;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import ru.job4j.accidents.Main;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.service.AccidentService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
class AccidentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccidentService accidentService;

    @Test
    @WithMockUser
    public void whenGetCreate() throws Exception {
        mockMvc.perform(get("/accident/create"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("accident/createAccident"));
    }

    @Test
    @WithMockUser
    public void whenGetEdit() throws Exception {
        when(accidentService.findById(anyInt())).thenReturn(Optional.of(new Accident()));

        mockMvc.perform(get("/accident/0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("accident/editAccident"));
    }

    @Test
    @WithMockUser
    public void whenGetEditButNotFoundById() throws Exception {
        when(accidentService.findById(anyInt())).thenReturn(Optional.empty());

        mockMvc.perform(get("/accident/0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("errors/404"));
    }

    @Test
    @WithMockUser
    public void whenGetDelete() throws Exception {
        mockMvc.perform(get("/accident/delete/1"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));
    }

    @Test
    @WithMockUser
    public void whenPostSave() throws Exception {
        Accident accident = new Accident();

        mockMvc.perform(post("/accident/save")
                        .flashAttr("accident", accident))
                .andDo(print())
                .andExpect(status().is3xxRedirection());

        ArgumentCaptor<Accident> argumentCaptor = ArgumentCaptor.forClass(Accident.class);
        verify(accidentService).add(argumentCaptor.capture(), any());

        assertThat(argumentCaptor.getValue()).isEqualTo(accident);
    }

    @Test
    @WithMockUser
    public void whenPostUpdate() throws Exception {
        Accident accident = new Accident();

        mockMvc.perform(post("/accident/update")
                        .flashAttr("accident", accident))
                .andDo(print())
                .andExpect(status().is3xxRedirection());

        ArgumentCaptor<Accident> argumentCaptor = ArgumentCaptor.forClass(Accident.class);
        verify(accidentService).update(argumentCaptor.capture(), any());

        assertThat(argumentCaptor.getValue()).isEqualTo(accident);
    }
}