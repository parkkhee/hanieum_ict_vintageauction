import React from "react";
import {Link, useNavigate} from "react-router-dom"
import axios from "axios";
import "../css/Navigation.css"
const Navigation = ({  memberObj, refreshMember }) => {
    const navigate = useNavigate()
    // 로그아웃 함수
    const SignOut = () => {
        // App.js에서 가져온 유저 정보 갱신 함수를 통해
        // 유저 정보 null로 변경
        refreshMember(null)
        // 서버에 로그아웃 요청
        axios.post('/api/members/logout/', )
        .then(response => {
            console.log(response.data);
        })
    }
    return (
    <nav>
        <ul className="navigator">
            <li>
                <Link to="/" className="Home">
                    Home
                </Link>
            </li>
            {/*유저 정보가 존재하면 My Profile, Sign Out표시 */}
            {/*null이면 로그인 및 회원가입 표시 */}
            {memberObj ? (
            <>
                <li>
                    <Link to="/profile" className="Profile">
                        My Profile
                    </Link>
                </li>
                <li>
                    <Link to="/" className="SignOut">
                    <span onClick={SignOut}>
                        Sign Out
                    </span>
                    </Link>
                </li>
            </>
            ) : (
                <>
                <li>
                    <Link to="/sign-up" className="SignUp">
                        Sign Up
                    </Link>
                </li>
                <li>
                    <Link to="/sign-in" className="SignIn">
                        Sign In
                    </Link>
                </li>
            </>
            )
            
            }
        </ul>
    </nav>
)}

export default Navigation