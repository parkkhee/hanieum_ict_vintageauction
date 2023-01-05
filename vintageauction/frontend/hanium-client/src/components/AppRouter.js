import React from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import SignUp from "../routes/SignUp";
import SignIn from "../routes/SignIn";
import Home from "../routes/Home";
import Profile from "../routes/Profile";
import Header from "./Header";
import VintageUpload from "../routes/VintageUpload";
import Vintage from "../routes/Vintage";
import VintageDetail from "../routes/VintageDetail";
import Auction from "../routes/Auction";
import AuctionUpload from "../routes/AuctionUpload";
import AuctionDetail from "../routes/AuctionDetail";
const AppRouter = ({ memberObj, refreshMember }) => {
    return (
        <Router>
            {/*헤더에 유저 정보 전달하여 로그인 유무에 따라 네비게이션 달리 표시 */}
            <Header memberObj={memberObj} refreshMember={refreshMember}/>
            <div className="router">
                <Routes>
                    <>
                        <Route path='/sign-up' element={<SignUp refreshMember={refreshMember}/>}/>
                        <Route path='/sign-in' element={<SignIn refreshMember={refreshMember}/>}/>
                        <Route path='/profile' element={<Profile memberObj={memberObj} refreshMember={refreshMember}/>}/>
                        <Route path='/vintage-upload' element={<VintageUpload memberObj={memberObj}/>}/>
                        <Route path='/vintages' element={<Vintage memberObj={memberObj}/>}/>
                        <Route path="/vintage/:vintageId" element={<VintageDetail memberObj={memberObj} refreshMember={refreshMember}/>}/>
                        <Route path='/' element={<Home/>}/>
                        <Route path='/auction' element={<Auction memberObj={memberObj}/>}/>
                        <Route path='/auction-upload' element={<AuctionUpload memberObj={memberObj}/>}/>
                        <Route path="/auction/:auctionId" element={<AuctionDetail memberObj={memberObj} refreshMember={refreshMember}/>}/>
                    </>
                </Routes>
            </div>
        </Router>
    )
}

export default AppRouter