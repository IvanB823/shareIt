package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ItemRequestMapperTest {

    @Test
    void toItemRequest_ShouldMapCorrectly() {
        LocalDateTime now = LocalDateTime.now();
        User user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");

        ItemRequestDto dto = new ItemRequestDto(1L, "Description", now, List.of());

        ItemRequest result = ItemRequestMapper.toItemRequest(dto, user);

        assertNotNull(result);
        assertEquals(dto.getId(), result.getId());
        assertEquals(dto.getDescription(), result.getDescription());
        assertEquals(now, result.getCreated());
        assertEquals(user, result.getRequester());
    }

    @Test
    void toItemRequest_WhenDtoIsNull_ShouldReturnNull() {
        ItemRequest result = ItemRequestMapper.toItemRequest(null, new User());
        assertNull(result);
    }

    @Test
    void toItemRequestDto_ItemRequest_ShouldMapCorrectly() {
        LocalDateTime now = LocalDateTime.now();
        ItemRequest request = new ItemRequest();
        request.setId(1L);
        request.setDescription("Desc");
        request.setCreated(now);
        request.setRequester(new User());

        ItemRequestDto result = ItemRequestMapper.toItemRequestDto(request);

        assertNotNull(result);
        assertEquals(request.getId(), result.getId());
        assertEquals(request.getDescription(), result.getDescription());
        assertEquals(now, result.getCreated());
        assertTrue(result.getItems().isEmpty());
    }

    @Test
    void toItemRequestDto_ItemRequestAndItems_ShouldMapCorrectly() {
        LocalDateTime now = LocalDateTime.now();
        ItemRequest request = new ItemRequest();
        request.setId(1L);
        request.setDescription("Desc");
        request.setCreated(now);
        request.setRequester(new User());

        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Item");
        itemDto.setRequestId(1L);

        ItemRequestDto result = ItemRequestMapper.toItemRequestDto(request, List.of(itemDto));

        assertNotNull(result);
        assertEquals(request.getId(), result.getId());
        assertEquals(request.getDescription(), result.getDescription());
        assertEquals(now, result.getCreated());
        assertNotNull(result.getItems());
        assertEquals(1, result.getItems().size());
        assertEquals("Item", result.getItems().get(0).getName());
    }
}