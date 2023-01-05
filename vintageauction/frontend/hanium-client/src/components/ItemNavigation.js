import React from "react";
import { Link } from "react-router-dom";
import "../css/Navigation.css";
const ItemNavigation = () => (
  <div className="item-navigater-container">
    <ul className="item-navigator">
      <li>
        <img className="icon1" src={require("../img/icon1.png")} />
        <Link to="/vintages" className="vintage">
          일반 거래
        </Link>
      </li>
      <div className="icon-box">
        <Link to="/auction">
          <img className="icon" src={require("../img/logo.png")} />
        </Link>
      </div>
      <li>
        <Link to="/auction" className="Auction">
          경매 거래
        </Link>
        <img className="icon2" src={require("../img/icon2.png")} />
      </li>
    </ul>
  </div>
);

export default ItemNavigation;
