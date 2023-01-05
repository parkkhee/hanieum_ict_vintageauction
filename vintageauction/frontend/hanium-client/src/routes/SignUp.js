import SignUpForm from "../components/SignUpForm";
import React from "react";
import "../css/SignUp.css"
const SignUp = ({refreshMember}) => {
    return (
        <>
            <div className="signUp-container">
                <h1>회원가입</h1>
                <SignUpForm refreshMember={refreshMember}/>
            </div>
        </>
    )
}

export default SignUp;