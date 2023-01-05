import { React, useState } from "react";
import { useRef } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { useEffect } from "react";
import "../css/Vintage.css";
const VintageInfo = ({ vintageId }) => {
  const [title, setTitle] = useState("");
  const [detail, setDetail] = useState("");
  const [itemName, setItemName] = useState("");
  const [price, setPrice] = useState(0);
  const [category, setCategory] = useState("");
  const [file, setFile] = useState(null);
  const [itemObj, setItemObj] = useState(null);
  const [postMode, setPostMode] = useState(false);

  const formData = new FormData();
  const fileInput = useRef();
  const navigate = useNavigate();

  const getItemInfo = () => {
    axios.get(`/api/vintage/${vintageId}`).then((response) => {
      setItemObj(response.data);
      setTitle(response.data.title);
      setItemName(response.data.itemName);
      setPrice(response.data.itemPrice);
      setDetail(response.data.detail);
      setCategory(response.data.itemCategory);
      setFile(response.data.storeFileName[0]);
    });
  };
  useEffect(getItemInfo, []);

  const changeMode = () => {
    setPostMode(true);
    var x = document.getElementsByClassName("readOnly");
    for (var i = 0; i < x.length; i++) {
      x[i].readOnly = false;
    }
  };

  const deal = () => {
    axios
      .post(`/api/vintage/deal`, {
        vintageBoardId: vintageId,
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
                  {price + " 원"}</div>
              <br />
              <label htmlFor="title-input" className="detail">
                설명
              </label>
              <div
                className="detail-input3">
                  {detail}</div>
            </div>
          </div>
        </div>
      ) : null}
    </>
  );
};

export default VintageInfo;
