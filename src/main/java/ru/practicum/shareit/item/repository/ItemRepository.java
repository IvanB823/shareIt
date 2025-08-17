package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByOwnerId(Long ownerId);

    @Query(value = "SELECT i FROM items i " +
            "WHERE i.is_available = true AND " +
            "(UPPER(i.name) LIKE UPPER(CONCAT('%', ?1, '%')) OR " +
            "UPPER(i.description) LIKE UPPER(CONCAT('%', ?1, '%')))", nativeQuery = true)
    List<Item> search(String text);
}
