import React, { useEffect, useRef } from "react";
import { Link, useNavigate } from "react-router-dom";
import axios from "axios";
import ChattingRoom from "../components/ChattingRoom";
import { useState } from "react";
import ChatCircle from "./ChatCircle";
const formData = new FormData();
const ProfileInfo = ({ memberObj, refreshMember }) => {
    const navigate = useNavigate();
    const [chatObj, setChatObj] = useState([]);
    const [modalOpen, setModalOpen] = useState(false);
    const [chatRoomList, setchatRoomList] = useState([]);
    const [file, setFile] = useState(null);
    const [userImg, setUserImg] = useState(null);
    const fileInput = useRef();

    // 유저 정보가 없으면 기본 페이지로 이동
    if (!memberObj) {
        navigate("/")
    }
    const chatList = chatRoomList && chatRoomList.map((item)=>{
        if (item.buyerNo.memberId === memberObj.memberId)
            return <ChatCircle onClick={(e) => {chat(item)}} setChatObj={setChatObj} setModalOpen={setModalOpen} item={item} member = {item.sellerNo}/>
        else
            return <ChatCircle onClick={(e) => {chat(item)}} setChatObj={setChatObj} setModalOpen={setModalOpen} item={item} member = {item.buyerNo}/>
      })
    const getChatRoomList = () => {
        console.log(memberObj)
        if (memberObj) {
            axios.get(`/api/chatroom`)
            .then(response => {
                console.log(response.data)
                setchatRoomList(response.data);
            })
        } else {
            return
        }
    }
    const chat = (chatObj) => {
        if (modalOpen){
            alert("이미 채팅방이 열려있습니다.");
            return;
        }
        setChatObj(chatObj);
        setModalOpen(true);
    }
    useEffect(() => {
        getChatRoomList();
        setUserImg(memberObj?.memberImgUrl);
    }, []);
    
    return (
        <>
        <div className="profile-container">
            <p className="profile-title">프로필</p>
            <div className="user-info">
                <div className="user-image">
                    <img className="item-image"
                        src={userImg ? require(`../memberProfiles/${userImg}`):(require("../img/temp.png"))}/>
                </div>
                <div className="user-text">
                    <div className="user-name">{memberObj?.memberName}님</div>
                    <div className="user-point">{memberObj?.point}</div>
                </div>
            </div>
            <div>
                <p>진행중인 채팅 목록</p>
                <div className="chat-list">
                    {chatList}
                </div>
            </div>
            {modalOpen && <ChattingRoom memberObj={memberObj} chatObj={chatObj} setOpenModal={setModalOpen}/>}
        </div>
        </>
    )
}

export default ProfileInfo