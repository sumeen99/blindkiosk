package com.blindkiosk.server;

import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreModel {
    private Long id;//가게 고유번호
    private String storeName; //가게명
    private Long storeLongitude;//가게경도
    private Long storeLatitude;//가게위도
    private Long storeCategory;//
    private List<String> material=new ArrayList<>();//재료
    private Map<String,ArrayList<String>> menu= new HashMap<>();//메뉴
}
