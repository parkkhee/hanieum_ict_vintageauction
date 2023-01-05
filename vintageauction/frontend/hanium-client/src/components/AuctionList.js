import React, { useEffect, useState } from "react";
import axios from "axios";
import Item from "../components/AuctionItem";
import "../css/Vintage.css";
import { Link } from "react-router-dom";
import Pagination from "react-js-pagination";

const AuctionList = () => {
  const [itemList, setItemList] = useState(null);
  const [totalPage, setTotalPage] = useState(0);
  const [page, setPage] = useState(0);

  // 렌더링 시 중고 상품 리스트 정보 요청
  const getItemList = (currentPage) => {
    if(currentPage) currentPage--;
    axios.get(`/api/auctions?page=${currentPage ?? page}`).then((response) => {
      setItemList(response.data.auctionBoardList);
      setTotalPage(response.data.totalPage);
      console.log(response.data);
    });
  };
  useEffect(getItemList, []);
  const handlePageChange = (page) => {
    setPage(page);
    getItemList(page);
  };
  return (
    <>
      <h1>경매 상품 리스트</h1>
      <div className="vintage-container">
        {/*setItemList 배열의 갯수만큼 Item 컴포넌트 생성 및 해당 컴포넌트에 아이템 정보 전달 */}
        {itemList &&
          itemList
            .slice(0)
            .map((item) => (
              <Link key={item.auctionId} to={`/auction/${item.auctionId}`}>
                <Item itemInfo={item} itemTitle={item.auctionTitle}/>
              </Link>
            ))}
      </div>
      <Pagination
        activePage={page}
        itemsCountPerPage={1}
        totalItemsCount={totalPage}
        pageRangeDisplayed={10}
        prevPageText={"‹"}
        nextPageText={"›"}
        onChange={handlePageChange}
        className="pagenation"
      />
    </>
  );
};

export default AuctionList;
