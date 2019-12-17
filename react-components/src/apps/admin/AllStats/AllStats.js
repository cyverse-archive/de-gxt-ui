import React from "react";
import "./AllStatsStyle.css";
import DatePicker from "./AppStatsComponents/datePicker.js";
import "./AppStatsComponents/button";
import ColorButton from "./AppStatsComponents/button";
import NavBarTabs from "./AppStatsComponents/NavBarTabs";
export default function DatePickers() {
    return (
        <div className="main-page">
            <header>
                <div className="datePickers">
                    <DatePicker label="Start Date" />
                    <DatePicker label="End Date" />
                    <ColorButton
                        variant="contained"
                        color="primary"
                        className="apply-filter-btn"
                    >
                        Apply Filter
                    </ColorButton>
                </div>
                <NavBarTabs />
            </header>
        </div>
    );
}
