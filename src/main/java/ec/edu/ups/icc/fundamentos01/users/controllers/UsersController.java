package ec.edu.ups.icc.fundamentos01.users.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ec.edu.ups.icc.fundamentos01.products.dtos.ProductsResponseDto;
import ec.edu.ups.icc.fundamentos01.users.dtos.CreateUserDto;
import ec.edu.ups.icc.fundamentos01.users.dtos.PartialUpdateUserDto;
import ec.edu.ups.icc.fundamentos01.users.dtos.UpdateUserDto;
import ec.edu.ups.icc.fundamentos01.users.dtos.UserResponseDto;
import ec.edu.ups.icc.fundamentos01.users.services.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

        @GetMapping
    public List<UserResponseDto> findAll() {
        return userService.findAll();
    }

        @GetMapping("/{id}")
    public UserResponseDto findOne(@PathVariable("id") int id) {
        return userService.findOne(id);
    }

        @PostMapping
    public ResponseEntity<UserResponseDto> create(
            @Valid @RequestBody CreateUserDto userDto
    ) {
        UserResponseDto created = userService.create(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

        @PutMapping("/{id}")
    public UserResponseDto update(@PathVariable("id") int id, @RequestBody UpdateUserDto dto) {
        return userService.update(id, dto);
    }


       @PatchMapping("/{id}")
    public UserResponseDto partialUpdate(
            @PathVariable("id") int id,
            @RequestBody PartialUpdateUserDto dto
    ) {
        return userService.partialUpdate(id, dto);
    }

        @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/products")
    // GET /api/users/{id}/products: productos del usuario.
    public List<ProductsResponseDto> findProductsByUserId(@PathVariable("id") Long id) {
        return userService.getProductsByUserId(id);
    }

    @GetMapping("/{id}/products-v2")
    public List<ProductsResponseDto> findProductsByUserIdWithFilters(
            @PathVariable("id") Long id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "minPrice", required = false) Double minPrice,
            @RequestParam(value = "maxPrice", required = false) Double maxPrice,
            @RequestParam(value = "categoryId", required = false) Long categoryId
    ) {
        return userService.getProductsByUserIdWithFilters(id, name, minPrice, maxPrice, categoryId);
    }

}
