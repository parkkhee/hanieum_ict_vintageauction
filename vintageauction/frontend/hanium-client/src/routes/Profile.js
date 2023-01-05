import React from "react";
import "../css/Profile.css"
import ProfileInfo from "../components/ProfileInfo";
import { useState } from "react";
import ProfileUpdate from "../components/ProfileUpdate";
const Profile = ({ memberObj, refreshMember }) => {
    const [editMode, setEditMode] = useState(false);
    const changeMode = () => {
        setEditMode(!editMode);
        console.log(editMode)
    }
    console.log(memberObj);
    return (
        <>
        {!editMode ?
        <div className="profile-route">
            <ProfileInfo memberObj={memberObj} refreshMember={refreshMember}/>
            <button className="mode-button"onClick={changeMode}>회원정보수정</button>
        </div>
        : <ProfileUpdate memberObj={memberObj} refreshMember={refreshMember}/>}
        </>
    )
}

export default Profile