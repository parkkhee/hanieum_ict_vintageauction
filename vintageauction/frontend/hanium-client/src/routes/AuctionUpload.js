import React from "react";
import AuctionUploadForm from "../components/AuctionUploadForm";
import "../css/Vintage.css"
const AuctionUpload = () => {

    return (
        <div className="vintage-upload-container">
            <h3>경매 상품 등록</h3>
            <AuctionUploadForm />
        </div>
    )
}

export default AuctionUpload;