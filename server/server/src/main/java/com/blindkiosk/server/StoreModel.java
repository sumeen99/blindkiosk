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
    private String storeName; //가게명
    private String storeNum;//가게위치
    private List<String> material=new ArrayList<>();//재료
    private Map<String,ArrayList<String>> menu= new HashMap<>();//메뉴

}
