import React from "react";
import "../css/Vintage.css";
import AuctionList from "../components/AuctionList";
import AuctionSearch from "../components/AuctionSearch";
import { useNavigate } from "react-router-dom";

const Auction = ({memberObj}) => {
  const navigate = useNavigate()

  const goToUpload = () => {
    if(!memberObj) {
      alert("로그인이 필요합니다.");
      return;
    }
    navigate('/auction-upload');
  }
  return (
    <>
      <div className="vintage-route">
        <button className="go-to-upload" onClick={goToUpload}>경매 상품 등록</button>
        <AuctionSearch />
        <AuctionList />
      </div>
    </>
  );
};

export default Auction;
