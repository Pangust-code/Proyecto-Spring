package ec.edu.ups.icc.fundamentos01.users.models;

import java.time.LocalDateTime;

import ec.edu.ups.icc.fundamentos01.exceptions.domain.BadRequestException;
import ec.edu.ups.icc.fundamentos01.users.dtos.CreateUserDto;
import ec.edu.ups.icc.fundamentos01.users.dtos.PartialUpdateUserDto;
import ec.edu.ups.icc.fundamentos01.users.dtos.UpdateUserDto;
import ec.edu.ups.icc.fundamentos01.users.dtos.UserResponseDto;
import ec.edu.ups.icc.fundamentos01.users.entities.UserEntity;

public class User {

     /// Variables de instancia

    /// Constructores 

    // Getters y Setters

    private int id;
    private String name;
    private String email;
    private String password;
    private LocalDateTime createdAt;

    public User(int id, String name, String email, String password) {
        if (name == null || name.isBlank())
            throw new BadRequestException("Nombre inválido");

        if (email == null || !email.contains("@"))
            throw new BadRequestException("Email inválido");

        if (password == null || password.length() < 8)
            throw new BadRequestException("Password inválido");

        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.createdAt = LocalDateTime.now();
    }

    // ==================== FACTORY METHODS ====================

    /**
     * Constructor completo incluyendo fechas (para conversiones desde entidad)
     */
    public User(int id, String name, String email, String password, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
    }

    public User update(UpdateUserDto dto) {
    this.name = dto.name;
    this.email = dto.email;    
    this.password = dto.password;
    return this;
    }

    public User partialUpdate(PartialUpdateUserDto dto) {
        if (dto.name != null) {
            this.name = dto.name;
        }
        if (dto.email != null) {
            this.email = dto.email;
        }
        if (dto.password != null) {
            this.password = dto.password;
        }
        return this;
    }

    /**
     * Crea un User desde una entidad persistente
     * @param entity Entidad recuperada de la BD
     * @return instancia de User para lógica de negocio
     */
    public static User fromEntity(UserEntity entity) {
        Long entityId = entity.getId();
        int id = entityId == null ? 0 : entityId.intValue();
        return new User(
            id,
            entity.getName(),
            entity.getEmail(),
            entity.getPassword(),
            entity.getCreatedAt()
        );
    }

    // ==================== CONVERSION METHODS ====================

    /**
     * Convierte este User a una entidad persistente
     * @return UserEntity lista para guardar en BD
     */
    public UserEntity toEntity() {
        UserEntity entity = new UserEntity();
        if (this.id > 0) {
            entity.setId((long) this.id);
        }
        entity.setName(this.name);
        
        // Preservar fechas si existen (para actualizaciones)
        if (this.createdAt != null) {
            entity.setCreatedAt(this.createdAt);
        }
        
        entity.setEmail(this.email);
        entity.setPassword(this.password);
        return entity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public static User fromDto(CreateUserDto dto) {
        User user = new User(0, dto.name, dto.email, dto.password);
        return user;
    }

    public UserResponseDto toResponseDto() {
        UserResponseDto dto = new UserResponseDto();
        dto.id = this.id;
        dto.name = this.name;
        dto.email = this.email;
        return dto;
    }
}