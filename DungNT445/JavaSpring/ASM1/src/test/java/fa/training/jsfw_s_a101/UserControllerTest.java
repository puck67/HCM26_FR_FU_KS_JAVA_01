package fa.training.jsfw_s_a101;

import fa.training.jsfw_s_a101.controller.UserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerTest {

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/templates/");
        viewResolver.setSuffix(".html");

        mockMvc = MockMvcBuilders.standaloneSetup(new UserController())
                .setValidator(validator)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    public void testGetRegisterForm() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    public void testSubmitFormWithErrors() throws Exception {
        mockMvc.perform(post("/register")
                        .param("firstName", "")
                        .param("lastName", "")
                        .param("email", "invalid-email")
                        .param("password", "short")
                        .param("age", "15"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("user", "firstName", "lastName", "email", "password", "age"));
    }

    @Test
    public void testSubmitFormSuccessfully() throws Exception {
        mockMvc.perform(post("/register")
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("email", "john.doe@example.com")
                        .param("password", "strongpassword")
                        .param("age", "25"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/registerSuccess"));
    }

    @Test
    public void testGetRegisterSuccess() throws Exception {
        mockMvc.perform(get("/registerSuccess"))
                .andExpect(status().isOk())
                .andExpect(view().name("registerSuccess"));
    }
}
