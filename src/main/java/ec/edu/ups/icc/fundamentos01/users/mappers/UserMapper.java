package ec.edu.ups.icc.fundamentos01.users.mappers;

import ec.edu.ups.icc.fundamentos01.users.dtos.CreateUserDto;
import ec.edu.ups.icc.fundamentos01.users.dtos.UserResponseDto;
import ec.edu.ups.icc.fundamentos01.users.models.User;

public class UserMapper {

    /**
     * Convierte un User a un DTO de respuesta
     * @param user Modelo de dominio
     * @return DTO con los datos públicos del usuario
     */
    public static UserResponseDto toResponse(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.id = user.getId();
        dto.name = user.getName();
        dto.email = user.getEmail();
        dto.createdAt = user.getCreatedAt();
        dto.updatedAt = user.getUpdatedAt();
        return dto;
    }

    /**
     * Convierte un DTO de creación a un User
     * @param dto DTO del formulario de creación
     * @return instancia de User
     */
    public static User fromCreateDto(CreateUserDto dto) {
        User user = new User(0, dto.name, dto.email, dto.password);
        return user;
    }

}