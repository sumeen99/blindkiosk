package com.blindkiosk.server.Model;

import lombok.*;



@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class StoreModel {
    private Long id;//가게 고유번호
    private String storeName; //가게명
    private MenuModel menu;//메뉴
}
