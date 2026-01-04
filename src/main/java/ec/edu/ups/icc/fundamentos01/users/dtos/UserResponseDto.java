package ec.edu.ups.icc.fundamentos01.users.dtos;

import java.time.LocalDateTime;

public class UserResponseDto {
    public int id;
    public String name;
    public String email;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;
}