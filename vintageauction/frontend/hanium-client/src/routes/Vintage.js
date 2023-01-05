import React, { useEffect, useState } from "react";
import "../css/Vintage.css";
import { Link, useNavigate } from "react-router-dom";
import VintageList from "../components/VintageList";
import VintageSearch from "../components/VintageSearch";

const Vintage = ({memberObj}) => {
  const navigate = useNavigate();
  const goToUpload = () => {
    if(!memberObj) {
      alert("로그인이 필요합니다.");
      return;
    }
    navigate('/vintage-upload');
  }
  return (
    <>
      <div className="vintage-route">
        <button className="go-to-upload" onClick={goToUpload}>중고 상품 등록</button>
        <VintageSearch />
        <VintageList />
      </div>
    </>
  );
};

export default Vintage;
