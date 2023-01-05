import React, {useState} from "react"
const ChatCircle =({setChatObj, setModalOpen, item, member}) => {
    //console.log(member);
    const chat = (chatObj) => {
        setChatObj(chatObj);
        setModalOpen(true);
    }
    return (
        <div className="chat-circle" onClick={()=> {
            chat(item);
        }}>
            <img src={member.memberImgUrl ? require(`../memberProfiles/${member.memberImgUrl}`):(require("../img/temp.png"))}></img>
            <p className="chat-item-name">{member.memberId}</p>
        </div>
    )

}

export default ChatCircle