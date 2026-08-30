import { useLocation, useNavigate } from "react-router-dom";

function DashboardLayout({ children, title, subtitle }) {
    const navigate = useNavigate();
    const location = useLocation();

    const dashboardActive =
        location.pathname === "/contacts" ||
        (location.pathname.startsWith("/contacts/") && location.pathname !== "/contacts/new");

    const addContactActive = location.pathname === "/contacts/new";
    const profileActive = location.pathname === "/profile";
    const contactToolsActive = location.pathname === "/contact-tools";

    function handleLogout() {
        localStorage.removeItem("token");
        navigate("/");
    }

    return (
        <div className="dashboard-layout">
            <aside className="sidebar">
            <div className="sidebar-brand">
                CMS
            </div>

            <button
                type="button"
                className={"sidebar-link" + (dashboardActive ? " sidebar-link-active" : "")}
                onClick={() => navigate("/contacts")}
            >
                Dashboard
            </button>

            <button
                type="button"
                className={"sidebar-link" + (addContactActive ? " sidebar-link-active" : "")}
                onClick={() => navigate("/contacts/new")}
            >
                Add Contact
            </button>

            <button
                type="button"
                className={"sidebar-link" + (contactToolsActive ? " sidebar-link-active" : "")}
                onClick={() => navigate("/contact-tools")}
            >
                Contact Tools
            </button>

            <button
                type="button"
                className={"sidebar-link" + (profileActive ? " sidebar-link-active" : "")}
                onClick={() => navigate("/profile")}
            >
                Profile
            </button>

            <button
                type="button"
                className="sidebar-logout"
                onClick={handleLogout}
            >
                Logout
            </button>
            </aside>

            <main className="dashboard-main">
                <header className="dashboard-header">
                <p className="dashboard-eyebrow">
                    CONTACT MANAGEMENT SYSTEM
                </p>

                <h1>{title}</h1>

                {subtitle && (
                    <p className="dashboard-subtitle">
                        {subtitle}
                    </p>
                )}
                </header>

                <div className="dashboard-content">
                    {children}
                </div>
            </main>
        </div>
    );
}

export default DashboardLayout;