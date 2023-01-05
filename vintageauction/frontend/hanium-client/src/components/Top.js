import { React, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { Link } from "react-router-dom";
import Item from "../components/Item";

import "../css/Top.css";

const Top = () => {
  const navigate = useNavigate();
  const [itemList, setItemList] = useState(null);
  const [search, setSearch] = useState("");
  // input 칸에 데이터가 바뀔때마다 리렌더링
  const onChange = (event) => {
    const {
      target: { name, value },
    } = event;
    if (name === "search") {
      setSearch(value);
    }
  };
  const onSubmit = async (event) => {
    // 회원가입 or 로그인 버튼 ( newAccount에 따라 )
    console.log("search complete");

    event.preventDefault();
    // 입력받은 데이터를 객체에 담아
    // 로그인 api에 post 요청 // /search?page=0&vintageTitle=${search}
    axios
      .get(`/api/vintages/search?page=0&vintageTitle=${search}`)
      .then((response) => {
        //response
        console.log(response.data.vintageBoardList);
        setItemList(response.data.vintageBoardList);
        console.log("성공");
      })
      .catch((error) => alert(error.response.data));
  };

  return (
    <>
      <div className="slider">배너 자리</div>
      <form onSubmit={onSubmit}>
        <input
          name="search"
          placeholder="search"
          required
          value={search}
          onChange={onChange}
          className="search-input"
          id="search-input"
        />
        <input
          type="submit"
          onclick="alert('hi')"
          className="searh-btn"
          value="searchTitle"
        />
      </form>

      <h1>검색 상품 리스트</h1>
      <div className="vintage-container">
        {/*setItemList 배열의 갯수만큼 Item 컴포넌트 생성 및 해당 컴포넌트에 아이템 정보 전달 */}
        {itemList &&
          itemList
            .slice(0)
            .reverse()
            .map((item) => (
              <Link key={item.vintageId} to={`/vintage/${item.vintageId}`}>
                <Item itemInfo={item} />
              </Link>
            ))}
      </div>
    </>
  );
};

export default Top;
