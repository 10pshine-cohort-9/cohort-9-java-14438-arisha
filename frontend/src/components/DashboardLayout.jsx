import { useNavigate } from "react-router-dom";

function DashboardLayout({ children, title, subtitle }) {
    const navigate = useNavigate();

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
                className="sidebar-link"
                onClick={() => navigate("/contacts")}
            >
                Contacts
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