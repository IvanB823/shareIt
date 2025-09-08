package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class UserDtoTest {

    @Autowired
    private JacksonTester<UserDto> jsonJacksonTester;

    @Test
    void serializeDeserializeTest() throws Exception {
        UserDto dto = new UserDto(1L, "John", "john@example.com");

        String expectedJson = "{\"id\": 1, \"name\": \"John\", \"email\": \"john@example.com\"}";

        assertThat(jsonJacksonTester.write(dto))
                .isEqualToJson(expectedJson);

        assertThat(jsonJacksonTester.parse(expectedJson))
                .isEqualTo(dto);
    }

    @Test
    void serializeWithNullIdTest() throws Exception {
        UserDto dto = new UserDto(null, "Jane", "jane@example.com");

        String expectedJson = "{\"id\": null, \"name\": \"Jane\", \"email\": \"jane@example.com\"}";

        assertThat(jsonJacksonTester.write(dto))
                .isEqualToJson(expectedJson);

        assertThat(jsonJacksonTester.parse(expectedJson))
                .isEqualTo(dto);
    }

    @Test
    void serializeWithSpecialCharactersTest() throws Exception {
        UserDto dto = new UserDto(2L, "Иван Петров", "ivan.petrov@тест.рф");

        String expectedJson = "{\"id\": 2, \"name\": \"Иван Петров\", \"email\": \"ivan.petrov@тест.рф\"}";

        assertThat(jsonJacksonTester.write(dto))
                .isEqualToJson(expectedJson);

        assertThat(jsonJacksonTester.parse(expectedJson))
                .isEqualTo(dto);
    }

    @Test
    void deserializeWithExtraFieldsTest() throws Exception {
        UserDto expectedDto = new UserDto(3L, "Bob", "bob@example.com");

        String jsonWithExtraFields = "{\"id\": 3, \"name\": \"Bob\", \"email\": \"bob@example.com\", \"age\": 25, \"city\": \"Moscow\"}";

        assertThat(jsonJacksonTester.parse(jsonWithExtraFields))
                .isEqualTo(expectedDto);
    }

    @Test
    void deserializeWithMissingOptionalFieldsTest() throws Exception {
        UserDto expectedDto = new UserDto(null, "Alice", "alice@example.com");

        String jsonWithMissingId = "{\"name\": \"Alice\", \"email\": \"alice@example.com\"}";

        assertThat(jsonJacksonTester.parse(jsonWithMissingId))
                .isEqualTo(expectedDto);
    }

    @Test
    void serializeEmptyStringsTest() throws Exception {
        UserDto dto = new UserDto(4L, "", "");

        String expectedJson = "{\"id\": 4, \"name\": \"\", \"email\": \"\"}";

        assertThat(jsonJacksonTester.write(dto))
                .isEqualToJson(expectedJson);

        assertThat(jsonJacksonTester.parse(expectedJson))
                .isEqualTo(dto);
    }

    @Test
    void serializeLongEmailTest() throws Exception {
        String longEmail = "very.long.email.address.for.testing.purposes@very.long.domain.name.example.com";
        UserDto dto = new UserDto(5L, "Test User", longEmail);

        String expectedJson = String.format("{\"id\": 5, \"name\": \"Test User\", \"email\": \"%s\"}", longEmail);

        assertThat(jsonJacksonTester.write(dto))
                .isEqualToJson(expectedJson);

        assertThat(jsonJacksonTester.parse(expectedJson))
                .isEqualTo(dto);
    }

    // Тесты для UserMapper
    @Test
    void mapperToUserDtoTest() {
        User user = new User(1L, "John", "john@example.com");
        UserDto userDto = UserMapper.toUserDto(user);

        assertThat(userDto.getId()).isEqualTo(user.getId());
        assertThat(userDto.getName()).isEqualTo(user.getName());
        assertThat(userDto.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    void mapperToUserTest() {
        UserDto userDto = new UserDto(1L, "John", "john@example.com");
        User user = UserMapper.toUser(userDto);

        assertThat(user.getId()).isEqualTo(userDto.getId());
        assertThat(user.getName()).isEqualTo(userDto.getName());
        assertThat(user.getEmail()).isEqualTo(userDto.getEmail());
    }

    @Test
    void mapperToUserDtoWithNullIdTest() {
        User user = new User(null, "Jane", "jane@example.com");
        UserDto userDto = UserMapper.toUserDto(user);

        assertThat(userDto.getId()).isNull();
        assertThat(userDto.getName()).isEqualTo(user.getName());
        assertThat(userDto.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    void mapperToUserWithNullIdTest() {
        UserDto userDto = new UserDto(null, "Jane", "jane@example.com");
        User user = UserMapper.toUser(userDto);

        assertThat(user.getId()).isNull();
        assertThat(user.getName()).isEqualTo(userDto.getName());
        assertThat(user.getEmail()).isEqualTo(userDto.getEmail());
    }

    @Test
    void mapperRoundTripTest() {
        User originalUser = new User(1L, "Test User", "test@example.com");

        // User -> UserDto -> User
        UserDto userDto = UserMapper.toUserDto(originalUser);
        User mappedUser = UserMapper.toUser(userDto);

        assertThat(mappedUser.getId()).isEqualTo(originalUser.getId());
        assertThat(mappedUser.getName()).isEqualTo(originalUser.getName());
        assertThat(mappedUser.getEmail()).isEqualTo(originalUser.getEmail());
    }

    @Test
    void mapperWithSpecialCharactersTest() {
        User user = new User(2L, "Анна Смирнова", "anna.smirnova@почта.рф");
        UserDto userDto = UserMapper.toUserDto(user);

        assertThat(userDto.getId()).isEqualTo(user.getId());
        assertThat(userDto.getName()).isEqualTo(user.getName());
        assertThat(userDto.getEmail()).isEqualTo(user.getEmail());

        User mappedBackUser = UserMapper.toUser(userDto);
        assertThat(mappedBackUser.getId()).isEqualTo(user.getId());
        assertThat(mappedBackUser.getName()).isEqualTo(user.getName());
        assertThat(mappedBackUser.getEmail()).isEqualTo(user.getEmail());
    }
}