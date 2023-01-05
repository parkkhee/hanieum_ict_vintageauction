import React, { useEffect, useRef } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { useState } from "react";
const formData = new FormData();
const ProfileUpdate = ({ memberObj, refreshMember }) => {
    const navigate = useNavigate();
    const [file, setFile] = useState({});
    const [attachment, setAttachment] = useState(memberObj.attachment);
    const [userImg, setUserImg] = useState("temp.png");
    const [userId, setUserId] = useState(memberObj?.memberId)
    const [userName, setUserName] = useState(memberObj?.memberName);
    const fileInput = useRef();

    const onFileChange = (event) => {
        // 이미지 입력 시 url을 읽는 함수
        const {target:{files}} = event;
        // const theFile = files[0];
        const theFile = event.target.files[0];
        const reader = new FileReader();
        reader.onloadend = (finishEvent) => {
            const {currentTarget: { result }} = finishEvent
            setAttachment(result)
        }
        reader.readAsDataURL(theFile);
        // setFile(`${memberObj.memberId}_${theFile.name}`);
        setFile(theFile);
        //console.log(`${memberObj.memberId}_${theFile.name}`);
    }

    const refreshMemberData = async () => {
        await axios.get("/api/member")
        .then(response => {
            console.log(response.data)
            refreshMember(response.data)
        }).catch(error => {
            console.log(error)
        })
    }

    const updateMemberData = async () => {
        console.log(file);
        formData.append("memberId", userId);
        formData.append("memberName", userName);
        // formData.append("memberPassword", memberObj.memberPassword)
        formData.append("memberImgUrl", file); 
        await axios.post(`/api/memberUpdate`, 
        formData, {headers: {
            'Content-Type': "multipart/form-data"
        }})
        .then(response => {
            console.log(response.data);
            refreshMemberData();
            navigate("/vintages")
            // alert("다시 로그인 해주세요.");
            // SignOut();
        })
        .catch(error => console.log(error.response.data))
    }
    const onChange = (event) => {
        const {target: {name, value}} = event;
        if(name === "user-name")
            setUserName(value);
        else if(name === "user-id")
            setUserId(value);
    }

    const cancel = () => {
        navigate("/");
    }
    
    // 유저 정보가 없으면 기본 페이지로 이동
    if (!memberObj) {
        navigate("/")
    }
    
    useEffect(() => {
        console.log(memberObj.memberImgUrl != null)
        if (memberObj.memberImgUrl != null)
            setUserImg(memberObj.memberImgUrl);
    }, []);

    return (
        <>
        <div className="profile-container">
            <p className="profile-title">프로필</p>
            <div className="user-info">
                <div className="user-image">
                    <img className="item-image"
                        src={attachment ? attachment : require(`../memberProfiles/${userImg}`)}/>
                        <br/>
                        <label htmlFor="item-image">이미지 등록</label>
                        <input 
                        type="file"
                        name="item-image"
                        id="item-image"
                        onChange={onFileChange}
                        ref={fileInput}
                        />
                </div>
                <div className="user-text">
                <label htmlFor="user-id">ID</label>
                    <br/>
                    <input className="user-id"
                    value={userId}
                    required
                    placeholder="id"
                    id="user-id"
                    name="user-id"
                    onChange={onChange}/>
                    <div className="user-point">{memberObj?.point}</div>
                    <label htmlFor="user-name">name</label>
                    <br/>
                    <input className="user-name"
                    value={userName}
                    required
                    placeholder="name"
                    id="user-name"
                    name="user-name"
                    onChange={onChange}/>
                    <div className="user-point">{memberObj?.point}</div>
                </div>
            </div>
            <button className="save-button" onClick={updateMemberData}>수정</button>
            <button className="return-button" onClick={cancel}>취소</button>
        </div>
        </>
    )
}

export default ProfileUpdate