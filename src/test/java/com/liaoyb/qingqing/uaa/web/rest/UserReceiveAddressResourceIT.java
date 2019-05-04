package com.liaoyb.qingqing.uaa.web.rest;

import com.liaoyb.qingqing.uaa.UaaApp;
import com.liaoyb.qingqing.uaa.config.SecurityBeanOverrideConfiguration;
import com.liaoyb.qingqing.uaa.domain.UserReceiveAddress;
import com.liaoyb.qingqing.uaa.repository.UserReceiveAddressRepository;
import com.liaoyb.qingqing.uaa.service.UserReceiveAddressService;
import com.liaoyb.qingqing.uaa.service.dto.UserReceiveAddressDTO;
import com.liaoyb.qingqing.uaa.service.mapper.UserReceiveAddressMapper;
import com.liaoyb.qingqing.uaa.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.liaoyb.qingqing.uaa.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link UserReceiveAddressResource} REST controller.
 */
@SpringBootTest(classes = UaaApp.class)
public class UserReceiveAddressResourceIT {

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final Integer DEFAULT_DEFAULT_STATUS = 1;
    private static final Integer UPDATED_DEFAULT_STATUS = 2;

    private static final String DEFAULT_POST_CODE = "AAAAAAAAAA";
    private static final String UPDATED_POST_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_PROVINCE = "AAAAAAAAAA";
    private static final String UPDATED_PROVINCE = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_REGION = "AAAAAAAAAA";
    private static final String UPDATED_REGION = "BBBBBBBBBB";

    private static final String DEFAULT_DETAIL_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_DETAIL_ADDRESS = "BBBBBBBBBB";

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_LAST_MODIFIED_BY = 1L;
    private static final Long UPDATED_LAST_MODIFIED_BY = 2L;

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private UserReceiveAddressRepository userReceiveAddressRepository;

    @Autowired
    private UserReceiveAddressMapper userReceiveAddressMapper;

    @Autowired
    private UserReceiveAddressService userReceiveAddressService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restUserReceiveAddressMockMvc;

    private UserReceiveAddress userReceiveAddress;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserReceiveAddressResource userReceiveAddressResource = new UserReceiveAddressResource(userReceiveAddressService);
        this.restUserReceiveAddressMockMvc = MockMvcBuilders.standaloneSetup(userReceiveAddressResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserReceiveAddress createEntity(EntityManager em) {
        UserReceiveAddress userReceiveAddress = new UserReceiveAddress();
        userReceiveAddress.setUserId(DEFAULT_USER_ID);
        userReceiveAddress.setName(DEFAULT_NAME);
        userReceiveAddress.setPhone(DEFAULT_PHONE);
        userReceiveAddress.setDefaultStatus(DEFAULT_DEFAULT_STATUS);
        userReceiveAddress.setPostCode(DEFAULT_POST_CODE);
        userReceiveAddress.setProvince(DEFAULT_PROVINCE);
        userReceiveAddress.setCity(DEFAULT_CITY);
        userReceiveAddress.setRegion(DEFAULT_REGION);
        userReceiveAddress.setDetailAddress(DEFAULT_DETAIL_ADDRESS);
        userReceiveAddress.setCreatedBy(DEFAULT_CREATED_BY);
        userReceiveAddress.setCreatedDate(DEFAULT_CREATED_DATE);
        userReceiveAddress.setLastModifiedBy(DEFAULT_LAST_MODIFIED_BY);
        userReceiveAddress.setLastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return userReceiveAddress;
    }

    @BeforeEach
    public void initTest() {
        userReceiveAddress = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserReceiveAddress() throws Exception {
        int databaseSizeBeforeCreate = userReceiveAddressRepository.findAll().size();

        // Create the UserReceiveAddress
        UserReceiveAddressDTO userReceiveAddressDTO = userReceiveAddressMapper.toDto(userReceiveAddress);
        restUserReceiveAddressMockMvc.perform(post("/api/user-receive-addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userReceiveAddressDTO)))
            .andExpect(status().isCreated());

        // Validate the UserReceiveAddress in the database
        List<UserReceiveAddress> userReceiveAddressList = userReceiveAddressRepository.findAll();
        assertThat(userReceiveAddressList).hasSize(databaseSizeBeforeCreate + 1);
        UserReceiveAddress testUserReceiveAddress = userReceiveAddressList.get(userReceiveAddressList.size() - 1);
        assertThat(testUserReceiveAddress.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testUserReceiveAddress.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUserReceiveAddress.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testUserReceiveAddress.getDefaultStatus()).isEqualTo(DEFAULT_DEFAULT_STATUS);
        assertThat(testUserReceiveAddress.getPostCode()).isEqualTo(DEFAULT_POST_CODE);
        assertThat(testUserReceiveAddress.getProvince()).isEqualTo(DEFAULT_PROVINCE);
        assertThat(testUserReceiveAddress.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testUserReceiveAddress.getRegion()).isEqualTo(DEFAULT_REGION);
        assertThat(testUserReceiveAddress.getDetailAddress()).isEqualTo(DEFAULT_DETAIL_ADDRESS);
        assertThat(testUserReceiveAddress.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testUserReceiveAddress.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testUserReceiveAddress.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testUserReceiveAddress.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void createUserReceiveAddressWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userReceiveAddressRepository.findAll().size();

        // Create the UserReceiveAddress with an existing ID
        userReceiveAddress.setId(1L);
        UserReceiveAddressDTO userReceiveAddressDTO = userReceiveAddressMapper.toDto(userReceiveAddress);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserReceiveAddressMockMvc.perform(post("/api/user-receive-addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userReceiveAddressDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserReceiveAddress in the database
        List<UserReceiveAddress> userReceiveAddressList = userReceiveAddressRepository.findAll();
        assertThat(userReceiveAddressList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = userReceiveAddressRepository.findAll().size();
        // set the field null
        userReceiveAddress.setUserId(null);

        // Create the UserReceiveAddress, which fails.
        UserReceiveAddressDTO userReceiveAddressDTO = userReceiveAddressMapper.toDto(userReceiveAddress);

        restUserReceiveAddressMockMvc.perform(post("/api/user-receive-addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userReceiveAddressDTO)))
            .andExpect(status().isBadRequest());

        List<UserReceiveAddress> userReceiveAddressList = userReceiveAddressRepository.findAll();
        assertThat(userReceiveAddressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = userReceiveAddressRepository.findAll().size();
        // set the field null
        userReceiveAddress.setName(null);

        // Create the UserReceiveAddress, which fails.
        UserReceiveAddressDTO userReceiveAddressDTO = userReceiveAddressMapper.toDto(userReceiveAddress);

        restUserReceiveAddressMockMvc.perform(post("/api/user-receive-addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userReceiveAddressDTO)))
            .andExpect(status().isBadRequest());

        List<UserReceiveAddress> userReceiveAddressList = userReceiveAddressRepository.findAll();
        assertThat(userReceiveAddressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = userReceiveAddressRepository.findAll().size();
        // set the field null
        userReceiveAddress.setPhone(null);

        // Create the UserReceiveAddress, which fails.
        UserReceiveAddressDTO userReceiveAddressDTO = userReceiveAddressMapper.toDto(userReceiveAddress);

        restUserReceiveAddressMockMvc.perform(post("/api/user-receive-addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userReceiveAddressDTO)))
            .andExpect(status().isBadRequest());

        List<UserReceiveAddress> userReceiveAddressList = userReceiveAddressRepository.findAll();
        assertThat(userReceiveAddressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDefaultStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = userReceiveAddressRepository.findAll().size();
        // set the field null
        userReceiveAddress.setDefaultStatus(null);

        // Create the UserReceiveAddress, which fails.
        UserReceiveAddressDTO userReceiveAddressDTO = userReceiveAddressMapper.toDto(userReceiveAddress);

        restUserReceiveAddressMockMvc.perform(post("/api/user-receive-addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userReceiveAddressDTO)))
            .andExpect(status().isBadRequest());

        List<UserReceiveAddress> userReceiveAddressList = userReceiveAddressRepository.findAll();
        assertThat(userReceiveAddressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPostCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = userReceiveAddressRepository.findAll().size();
        // set the field null
        userReceiveAddress.setPostCode(null);

        // Create the UserReceiveAddress, which fails.
        UserReceiveAddressDTO userReceiveAddressDTO = userReceiveAddressMapper.toDto(userReceiveAddress);

        restUserReceiveAddressMockMvc.perform(post("/api/user-receive-addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userReceiveAddressDTO)))
            .andExpect(status().isBadRequest());

        List<UserReceiveAddress> userReceiveAddressList = userReceiveAddressRepository.findAll();
        assertThat(userReceiveAddressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProvinceIsRequired() throws Exception {
        int databaseSizeBeforeTest = userReceiveAddressRepository.findAll().size();
        // set the field null
        userReceiveAddress.setProvince(null);

        // Create the UserReceiveAddress, which fails.
        UserReceiveAddressDTO userReceiveAddressDTO = userReceiveAddressMapper.toDto(userReceiveAddress);

        restUserReceiveAddressMockMvc.perform(post("/api/user-receive-addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userReceiveAddressDTO)))
            .andExpect(status().isBadRequest());

        List<UserReceiveAddress> userReceiveAddressList = userReceiveAddressRepository.findAll();
        assertThat(userReceiveAddressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = userReceiveAddressRepository.findAll().size();
        // set the field null
        userReceiveAddress.setCity(null);

        // Create the UserReceiveAddress, which fails.
        UserReceiveAddressDTO userReceiveAddressDTO = userReceiveAddressMapper.toDto(userReceiveAddress);

        restUserReceiveAddressMockMvc.perform(post("/api/user-receive-addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userReceiveAddressDTO)))
            .andExpect(status().isBadRequest());

        List<UserReceiveAddress> userReceiveAddressList = userReceiveAddressRepository.findAll();
        assertThat(userReceiveAddressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRegionIsRequired() throws Exception {
        int databaseSizeBeforeTest = userReceiveAddressRepository.findAll().size();
        // set the field null
        userReceiveAddress.setRegion(null);

        // Create the UserReceiveAddress, which fails.
        UserReceiveAddressDTO userReceiveAddressDTO = userReceiveAddressMapper.toDto(userReceiveAddress);

        restUserReceiveAddressMockMvc.perform(post("/api/user-receive-addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userReceiveAddressDTO)))
            .andExpect(status().isBadRequest());

        List<UserReceiveAddress> userReceiveAddressList = userReceiveAddressRepository.findAll();
        assertThat(userReceiveAddressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDetailAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = userReceiveAddressRepository.findAll().size();
        // set the field null
        userReceiveAddress.setDetailAddress(null);

        // Create the UserReceiveAddress, which fails.
        UserReceiveAddressDTO userReceiveAddressDTO = userReceiveAddressMapper.toDto(userReceiveAddress);

        restUserReceiveAddressMockMvc.perform(post("/api/user-receive-addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userReceiveAddressDTO)))
            .andExpect(status().isBadRequest());

        List<UserReceiveAddress> userReceiveAddressList = userReceiveAddressRepository.findAll();
        assertThat(userReceiveAddressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUserReceiveAddresses() throws Exception {
        // Initialize the database
        userReceiveAddressRepository.saveAndFlush(userReceiveAddress);

        // Get all the userReceiveAddressList
        restUserReceiveAddressMockMvc.perform(get("/api/user-receive-addresses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userReceiveAddress.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].defaultStatus").value(hasItem(DEFAULT_DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].postCode").value(hasItem(DEFAULT_POST_CODE.toString())))
            .andExpect(jsonPath("$.[*].province").value(hasItem(DEFAULT_PROVINCE.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].region").value(hasItem(DEFAULT_REGION.toString())))
            .andExpect(jsonPath("$.[*].detailAddress").value(hasItem(DEFAULT_DETAIL_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getUserReceiveAddress() throws Exception {
        // Initialize the database
        userReceiveAddressRepository.saveAndFlush(userReceiveAddress);

        // Get the userReceiveAddress
        restUserReceiveAddressMockMvc.perform(get("/api/user-receive-addresses/{id}", userReceiveAddress.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userReceiveAddress.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()))
            .andExpect(jsonPath("$.defaultStatus").value(DEFAULT_DEFAULT_STATUS))
            .andExpect(jsonPath("$.postCode").value(DEFAULT_POST_CODE.toString()))
            .andExpect(jsonPath("$.province").value(DEFAULT_PROVINCE.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.region").value(DEFAULT_REGION.toString()))
            .andExpect(jsonPath("$.detailAddress").value(DEFAULT_DETAIL_ADDRESS.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY.intValue()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUserReceiveAddress() throws Exception {
        // Get the userReceiveAddress
        restUserReceiveAddressMockMvc.perform(get("/api/user-receive-addresses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserReceiveAddress() throws Exception {
        // Initialize the database
        userReceiveAddressRepository.saveAndFlush(userReceiveAddress);

        int databaseSizeBeforeUpdate = userReceiveAddressRepository.findAll().size();

        // Update the userReceiveAddress
        UserReceiveAddress updatedUserReceiveAddress = userReceiveAddressRepository.findById(userReceiveAddress.getId()).get();
        // Disconnect from session so that the updates on updatedUserReceiveAddress are not directly saved in db
        updatedUserReceiveAddress.setUserId(UPDATED_USER_ID);
        updatedUserReceiveAddress.setName(UPDATED_NAME);
        updatedUserReceiveAddress.setPhone(UPDATED_PHONE);
        updatedUserReceiveAddress.setDefaultStatus(UPDATED_DEFAULT_STATUS);
        updatedUserReceiveAddress.setPostCode(UPDATED_POST_CODE);
        updatedUserReceiveAddress.setProvince(UPDATED_PROVINCE);
        updatedUserReceiveAddress.setCity(UPDATED_CITY);
        updatedUserReceiveAddress.setRegion(UPDATED_REGION);
        updatedUserReceiveAddress.setDetailAddress(UPDATED_DETAIL_ADDRESS);
        updatedUserReceiveAddress.setCreatedBy(UPDATED_CREATED_BY);
        updatedUserReceiveAddress.setCreatedDate(UPDATED_CREATED_DATE);
        updatedUserReceiveAddress.setLastModifiedBy(UPDATED_LAST_MODIFIED_BY);
        updatedUserReceiveAddress.setLastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        UserReceiveAddressDTO userReceiveAddressDTO = userReceiveAddressMapper.toDto(updatedUserReceiveAddress);

        restUserReceiveAddressMockMvc.perform(put("/api/user-receive-addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userReceiveAddressDTO)))
            .andExpect(status().isOk());

        // Validate the UserReceiveAddress in the database
        List<UserReceiveAddress> userReceiveAddressList = userReceiveAddressRepository.findAll();
        assertThat(userReceiveAddressList).hasSize(databaseSizeBeforeUpdate);
        UserReceiveAddress testUserReceiveAddress = userReceiveAddressList.get(userReceiveAddressList.size() - 1);
        assertThat(testUserReceiveAddress.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testUserReceiveAddress.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUserReceiveAddress.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testUserReceiveAddress.getDefaultStatus()).isEqualTo(UPDATED_DEFAULT_STATUS);
        assertThat(testUserReceiveAddress.getPostCode()).isEqualTo(UPDATED_POST_CODE);
        assertThat(testUserReceiveAddress.getProvince()).isEqualTo(UPDATED_PROVINCE);
        assertThat(testUserReceiveAddress.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testUserReceiveAddress.getRegion()).isEqualTo(UPDATED_REGION);
        assertThat(testUserReceiveAddress.getDetailAddress()).isEqualTo(UPDATED_DETAIL_ADDRESS);
        assertThat(testUserReceiveAddress.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testUserReceiveAddress.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testUserReceiveAddress.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testUserReceiveAddress.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingUserReceiveAddress() throws Exception {
        int databaseSizeBeforeUpdate = userReceiveAddressRepository.findAll().size();

        // Create the UserReceiveAddress
        UserReceiveAddressDTO userReceiveAddressDTO = userReceiveAddressMapper.toDto(userReceiveAddress);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserReceiveAddressMockMvc.perform(put("/api/user-receive-addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userReceiveAddressDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserReceiveAddress in the database
        List<UserReceiveAddress> userReceiveAddressList = userReceiveAddressRepository.findAll();
        assertThat(userReceiveAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteUserReceiveAddress() throws Exception {
        // Initialize the database
        userReceiveAddressRepository.saveAndFlush(userReceiveAddress);

        int databaseSizeBeforeDelete = userReceiveAddressRepository.findAll().size();

        // Delete the userReceiveAddress
        restUserReceiveAddressMockMvc.perform(delete("/api/user-receive-addresses/{id}", userReceiveAddress.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<UserReceiveAddress> userReceiveAddressList = userReceiveAddressRepository.findAll();
        assertThat(userReceiveAddressList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserReceiveAddress.class);
        UserReceiveAddress userReceiveAddress1 = new UserReceiveAddress();
        userReceiveAddress1.setId(1L);
        UserReceiveAddress userReceiveAddress2 = new UserReceiveAddress();
        userReceiveAddress2.setId(userReceiveAddress1.getId());
        assertThat(userReceiveAddress1).isEqualTo(userReceiveAddress2);
        userReceiveAddress2.setId(2L);
        assertThat(userReceiveAddress1).isNotEqualTo(userReceiveAddress2);
        userReceiveAddress1.setId(null);
        assertThat(userReceiveAddress1).isNotEqualTo(userReceiveAddress2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserReceiveAddressDTO.class);
        UserReceiveAddressDTO userReceiveAddressDTO1 = new UserReceiveAddressDTO();
        userReceiveAddressDTO1.setId(1L);
        UserReceiveAddressDTO userReceiveAddressDTO2 = new UserReceiveAddressDTO();
        assertThat(userReceiveAddressDTO1).isNotEqualTo(userReceiveAddressDTO2);
        userReceiveAddressDTO2.setId(userReceiveAddressDTO1.getId());
        assertThat(userReceiveAddressDTO1).isEqualTo(userReceiveAddressDTO2);
        userReceiveAddressDTO2.setId(2L);
        assertThat(userReceiveAddressDTO1).isNotEqualTo(userReceiveAddressDTO2);
        userReceiveAddressDTO1.setId(null);
        assertThat(userReceiveAddressDTO1).isNotEqualTo(userReceiveAddressDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(userReceiveAddressMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(userReceiveAddressMapper.fromId(null)).isNull();
    }
}
