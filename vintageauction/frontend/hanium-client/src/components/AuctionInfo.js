import { React, useState } from "react";
import { useRef } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { useEffect } from "react";
import "../css/Vintage.css";
const VintageInfo = ({ auctionId, memberObj }) => {
  const [title, setTitle] = useState("");
  const [detail, setDetail] = useState("");
  const [itemName, setItemName] = useState("");
  const [bidPrice, setBidPrice] = useState(0);
  const [category, setCategory] = useState("");
  const [sellerId, setSellerId] = useState("");
  const [file, setFile] = useState(null);
  const [itemObj, setItemObj] = useState(null);

  const navigate = useNavigate();

  const getItemInfo = () => {
    axios.get(`/api/auction/${auctionId}`).then((response) => {
        console.log(response.data);
        setItemObj(response.data);
        setTitle(response.data.title);
        setItemName(response.data.itemName);
        setBidPrice(response.data.itemBidPrice);
        setDetail(response.data.detail);
        setCategory(response.data.itemCategory);
        setFile(response.data.storeFileName[0]);
        setSellerId(response.data.memberId);
    });
  };
  useEffect(getItemInfo, []);

  const bidding = () => {
    const new_bidding = parseInt(prompt("입찰가격을 입력하세요."));
    if (memberObj.point < new_bidding){
        alert("포인트가 부족합니다.");
        return;
    }
    else if (new_bidding <= bidPrice) {
        alert("현재 가격보다 높은 금액만 입찰 가능합니다.");
        return;
    }
    axios.post(`/api/auction/${auctionId}/bid`, {
        bidPrice: new_bidding,
      })
      .then((response) => {
        console.log(response.data);
      });
  };
  return (
    <>
      {itemObj ? (
        <div className="item-info-container">
          <div className="category-detail" >
            {category}</div>
          <div className="content-title">
            {title}</div>
          <div className="item-info">
            <div className="image-info">
              <img
                className="item-image"
                src={
                  file
                    ? require(`../itemImages/${file}`)
                    : require("../img/temp.png")
                }
              />
            </div>
            <div className="text-info">
              <label htmlFor="title-input" className="detail">
                상품명
              </label>
              <div className="detail-input1">{itemName}</div>
              <br />
              <label htmlFor="title-input" className="detail">
                가격
              </label>
              <div
                className="detail-input2">
                  {bidPrice + " 원"}</div>
              <br />
              <label htmlFor="title-input" className="detail">
                설명
              </label>
              <div
                className="detail-input3">
                  {detail}</div>
            </div>
          </div>
          {memberObj.memberId === sellerId ? null : 
          (
            <div>
            <button className="bidButton"onClick={bidding}>입찰</button>
            </div>)}
        </div>
      ) : null}
    </>
  );
};

export default VintageInfo;
