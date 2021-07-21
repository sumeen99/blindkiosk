### server guide for 주미



기본적으로 "음료"의 여러 메뉴들을 알고싶다면 "음료"의 id를 알고있어야함.
즉, 하위메뉴를 알고싶다면 현재메뉴의 id를 알고있어야한다!
어차피 주미는 가게->메뉴->하위메뉴->음식(?) 느낌으로 들어가니까 이런식으로 했구
영현이꺼(해당 store의 모든 food검색)는 하려면 데이터 좀 수정해서 해야할둣둣

* "맥도날드"(store)의 하위메뉴(Category)를 알고싶다 -> 기본적으로 store_id를 가져와야함
  쿼리:menu?name=맥도날드
  http://ec2-18-117-207-121.us-east-2.compute.amazonaws.com:8080/menu?name=맥도날드
  -> {"data":{"_id":"60e8903981a13384e4109faa","name":"맥도날드"},"errors":[]}

* 여기서 _id(60e8903981a13384e4109faa)를 저장하고 

  쿼리:category?id=60e8903981a13384e4109faa
  http://ec2-18-117-207-121.us-east-2.compute.amazonaws.com:8080/category?id=60e8903981a13384e4109faa
  ->{"data":[{"_id":"60e8941b81a13384e4109fc0","storeId":"60e8903981a13384e4109faa","name":"버거"},{"_id":"60e8944881a13384e4109fc1","storeId":"60e8903981a13384e4109faa","name":"해피 스낵"},{"_id":"60e8949981a13384e4109fc3","storeId":"60e8903981a13384e4109faa","name":"사이드&디저트"},{"_id":"60e8959581a13384e4109fc4","storeId":"60e8903981a13384e4109faa","name":"맥카페&음료"}],"errors":[]}

이 데이터에서 name을 추출하시면 될것같아울
만약 "버거"의 하위카테고리를 불러오고 싶다면 "버거"의 id를 저장해야겠죠?

* query목록
  * menu?name={}
  * category?id={}
  * subcategory?id={}
  * food?id={}