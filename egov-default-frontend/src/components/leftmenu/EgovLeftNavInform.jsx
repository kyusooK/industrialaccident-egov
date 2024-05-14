import React from 'react';

import URL from 'constants/url';
import { NavLink } from 'react-router-dom';

function EgovLeftNavInform() { 
    const lastSegment = window.parent.location.href.split('/').pop();
    
    if (lastSegment === 'accidents'){
        return (
            <div className="nav">
                <div className="inner">
                    <h2>산재접수</h2>
                    <ul className="menu4">
                        <li><NavLink to="/accident/accidents" className={({ isActive }) => (isActive ? "cur" : "")}>산재접수</NavLink></li>
                    </ul>
                </div>
            </div>
        );
    }
    if (lastSegment === 'assessments'){
        return (
            <div className="nav">
                <div className="inner">
                    <h2>요양급여</h2>
                    <ul className="menu4">
                        <li><NavLink to="/assessment/assessments" className={({ isActive }) => (isActive ? "cur" : "")}>요양급여</NavLink></li>
                    </ul>
                </div>
            </div>
        );
    }
    if (lastSegment === 'sickLeaves'){
        return (
            <div className="nav">
                <div className="inner">
                    <h2>휴업급여</h2>
                    <ul className="menu4">
                        <li><NavLink to="/assessment/sickLeaves" className={({ isActive }) => (isActive ? "cur" : "")}>휴업급여</NavLink></li>
                    </ul>
                </div>
            </div>
        );
    }
    if (lastSegment === 'compensations'){
        return (
            <div className="nav">
                <div className="inner">
                    <h2>보상지급</h2>
                    <ul className="menu4">
                        <li><NavLink to="/compensation/compensations" className={({ isActive }) => (isActive ? "cur" : "")}>보상지급</NavLink></li>
                    </ul>
                </div>
            </div>
        );
    }
    if (lastSegment === 'statistics'){
        return (
            <div className="nav">
                <div className="inner">
                    <h2>산재신청 처리통계</h2>
                    <ul className="menu4">
                        <li style={{marginBottom : '-15px'}}><NavLink to="/statistics/statistics" className={({ isActive }) => (isActive ? "cur" : "")}>심사결과 통계</NavLink></li>
                    </ul>
                    <ul className="menu4">
                        <li style={{marginBottom : '-15px'}}>회사별 통계</li>
                    </ul>
                    <ul className="menu4">
                        <li style={{marginBottom : '-15px'}}>병원별 통계</li>
                    </ul>
                    <ul className="menu4">
                        <li>날짜별 통계</li>
                    </ul>
                </div>
            </div>
        );
    }
    return null;
}

export default React.memo(EgovLeftNavInform);