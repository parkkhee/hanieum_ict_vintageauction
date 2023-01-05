import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";
import "../css/Vintage.css"
import VintageInfo from "../components/VintageInfo";
import VintageUpdateForm from "../components/VintageUpdateForm";
import ChattingRoom from "../components/ChattingRoom";

const VintageDetail = ({memberObj, refreshMember}) => {
    const [itemObj, setItemObj] = useState(null);
    const [postMode, setPostMode] = useState(false);
    const [modalOpen, setModalOpen] = useState(false);
    const [chatObj, setChatObj] = useState(null);
    const vintageId = useParams().vintageId;

    const navigate = useNavigate();

    const getItemInfo = () => {
        axios.get(`/api/vintage/${vintageId}`)
        .then(response => {
        setItemObj(response.data);
        console.log(response.data);
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

    const deletePost = () => {
        console.log(itemObj)
        if (window.confirm("삭제하시겠습니까?"))
        {
            if (memberObj) {
                axios.post(`/api/vintage/${vintageId}/delete`)
                .then(response => {
                    navigate("/");
                    console.log(response.data)
                })
                .catch(error => {
                    console.log(error)
                })
            }
        }
    }

    const chat =() => {
        if (memberObj) {
            axios.post(`/api/chat/new?receiverNo=${itemObj.memberNo}&itemId=${vintageId}`)
            .then(response => {
                setChatObj(response.data)
                console.log(response.data)
                setModalOpen(true);
            })
        } else {
            alert("로그인이 필요합니다.")
        }
    }

    return (
        <>
                {itemObj ? <div className="vintage-detail-container">
                    {postMode ? 
                        <VintageUpdateForm vintageId={vintageId} itemInfo={itemObj}/>
                         : <VintageInfo vintageId={vintageId}/> }
                    {memberObj?.memberId === itemObj.memberId ? 
                    <>{!postMode ? 
                    <div>
                        <button id="modeButton" onClick={changeMode}>수정</button>
                        <button id="deleteButton" onClick={deletePost}>삭제</button>   
                    </div> : null}
                    </> : 
                    <div>
                        <button className="openModalBtn" onClick={chat}>
                            채팅
                        </button>
                        {modalOpen && <ChattingRoom refreshMember={refreshMember} memberObj={memberObj} chatObj={chatObj} chat={chat}setOpenModal={setModalOpen}/>}
                    </div>}
                    </div> : null}
                    
                    
            
        </>
    )
}

export default VintageDetail