import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import DashboardLayout from "../components/DashboardLayout";

function ProfilePage() {
    const [profile, setProfile] = useState(null);
    const [error, setError] = useState("");

    const navigate = useNavigate();

    useEffect(() => {
        async function loadProfile() {
            const token = localStorage.getItem("token");

            if (!token) {
                navigate("/");
                return;
            }

            try {
                const response = await fetch("/api/auth/profile", {
                    headers: {
                        Authorization: "Bearer " + token,
                    },
                });

                if (response.status === 401 || response.status === 403) {
                    localStorage.removeItem("token");
                    navigate("/");
                    return;
                }

                if (!response.ok) {
                    setError("Unable to load profile");
                    return;
                }

                const data = await response.json();
                setProfile(data);
            } catch {
                setError("Unable to connect to the server");
            }
        }

        loadProfile();
    }, [navigate]);

    return (
        <DashboardLayout
            title="Profile"
            subtitle="View and manage your account information."
        >
            {error && <p>{error}</p>}

            {profile && (
                <div>
                    <p>Full Name: {profile.fullName}</p>
                    <p>Email: {profile.email || "Not provided"}</p>
                    <p>
                        Phone Number:{" "}
                        {profile.phoneNumber || "Not provided"}
                    </p>
                </div>
            )}
        </DashboardLayout>
    );
}

export default ProfilePage;