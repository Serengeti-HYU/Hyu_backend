package com.serengeti.hyu.backend.emoji.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

@Data
public class CreateEmojiDto {
    @NotBlank(message = "Image URL cannot be blank")
    private String imageUrl;
}
