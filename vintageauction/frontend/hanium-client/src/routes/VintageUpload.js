import React from "react";
import VintageUploadForm from "../components/VintageUploadForm";
import "../css/Vintage.css"
const VintageUpload = () => {

    return (
        <div className="vintage-upload-container">
            <h3>중고 상품 등록</h3>
            <VintageUploadForm/>
        </div>
    )
}

export default VintageUpload;