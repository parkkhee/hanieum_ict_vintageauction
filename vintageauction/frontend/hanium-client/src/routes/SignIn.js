import React from "react";
import SignInForm from "../components/SignInForm";
import "../css/SignIn.css"
const SignIn = ({ refreshMember }) => {
    return (
        <>
            <div className="signIn-container">
                <h1>로그인</h1>
                <SignInForm refreshMember={refreshMember}/>
            </div>
        </>
    )
}

export default SignIn;