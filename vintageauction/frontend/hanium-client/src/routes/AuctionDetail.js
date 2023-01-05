import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import axios from "axios";
import "../css/Vintage.css"
import AuctionInfo from "../components/AuctionInfo";
import AuctionUpdateForm from "../components/AuctionUpdateForm";

const AuctionDetail = ({memberObj, refreshMember}) => {
    const [itemObj, setItemObj] = useState(null);
    const [postMode, setPostMode] = useState(false);
    const auctionId = useParams().auctionId;
    const navigate = useNavigate();

    const getItemInfo = () => {
        axios.get(`/api/auction/${auctionId}`)
        .then(response => {
        setItemObj(response.data);
        console.log(response.data);
        if (response.data.auctionStatus !== "PROGRESSING")
        {
            alert("경매가 종료된 상품입니다.")
            navigate('/auction');
        }
    })
    }
    useEffect(getItemInfo, []);
    
    const changeMode = () => {
        if (postMode) {
            setPostMode(false);
            document.getElementById("modeButton").innerHTML = "수정"
        } else {
            setPostMode(true);
            document.getElementById("modeButton").innerHTML = "취소"
        }
    }

    return (
        <>
                {itemObj ? <div className="vintage-detail-container">
                    {postMode ? 
                        <AuctionUpdateForm auctionId={auctionId} itemInfo={itemObj}/>
                         : <AuctionInfo auctionId={auctionId} memberObj={memberObj}/> }
                    {memberObj?.memberId === itemObj.memberId ? 
                    <button id="modeButton"onClick={changeMode}>수정</button> : null}
                    </div> : null}
                    
                    
            
        </>
    )
}

export default AuctionDetail