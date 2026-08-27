import { useNavigate } from "react-router-dom";

function DashboardLayout({ children, title, subtitle }) {
    const navigate = useNavigate();

    function handleLogout() {
        localStorage.removeItem("token");
        navigate("/");
    }

    return (
        <div>
            <aside>
                <h2>CMS</h2>

                <button
                    type="button"
                    onClick={() => navigate("/contacts")}
                >
                    Contacts
                </button>

                <button
                    type="button"
                    onClick={handleLogout}
                >
                    Logout
                </button>
            </aside>

            <main>
                <h1>{title}</h1>

                {subtitle && <p>{subtitle}</p>}

                {children}
            </main>
        </div>
    );
}

export default DashboardLayout;