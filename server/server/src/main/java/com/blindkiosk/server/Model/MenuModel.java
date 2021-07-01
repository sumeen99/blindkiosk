package com.blindkiosk.server.Model;


import lombok.*;

import java.util.Map;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuModel {
    private Map<String,CategoryModel> category;
}
