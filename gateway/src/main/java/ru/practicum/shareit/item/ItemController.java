package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import static ru.practicum.shareit.util.Constants.USER_ID_HEADER;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader(USER_ID_HEADER) Long userId,
                                             @RequestBody @Valid ItemDto itemDto) {
        if (itemDto.getName() == null || itemDto.getName().trim().isEmpty()) {
            throw new ValidationException("Название предмета не должно быть пустым");
        }
        if (itemDto.getDescription() == null || itemDto.getDescription().trim().isEmpty()) {
            throw new ValidationException("Описание предмета не должно быть пустым");
        }
        if (itemDto.getAvailable() == null) {
            throw new ValidationException("Статус доступности не должно быть null");
        }
        return itemClient.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader(USER_ID_HEADER) Long userId,
                                             @PathVariable Long itemId,
                                             @RequestBody ItemDto itemDto) {

        return itemClient.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@PathVariable Long itemId,
                                          @RequestHeader(USER_ID_HEADER) Long userId) {
        return itemClient.getItem(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getItems(@RequestHeader(USER_ID_HEADER) Long userId) {
        return itemClient.getItems(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestParam String text) {
        return itemClient.searchItems(text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(USER_ID_HEADER) Long userId,
                                             @PathVariable Long itemId,
                                             @RequestBody @Valid CommentDto commentDto) {
        if (commentDto.getText() == null || commentDto.getText().trim().isEmpty()) {
            throw new ValidationException("Комментарий не должен быть пустым");
        }
        return itemClient.addComment(userId, itemId, commentDto);
    }
}