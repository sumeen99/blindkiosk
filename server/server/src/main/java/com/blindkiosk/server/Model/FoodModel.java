package com.blindkiosk.server.Model;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodModel {
    private List<String> material;
}
