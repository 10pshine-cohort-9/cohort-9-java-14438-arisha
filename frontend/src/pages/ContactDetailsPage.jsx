import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";

function ContactDetailsPage() {
    const [contact, setContact] = useState(null);
    const [error, setError] = useState("");
    const [emails, setEmails] = useState([]);
    const [phones, setPhones] = useState([]);
    const [newEmailAddress, setNewEmailAddress] = useState("");
    const [newEmailLabel, setNewEmailLabel] = useState("");

    const { id } = useParams();
    const navigate = useNavigate();

    useEffect(() => {
        async function loadContact() {
            const token = localStorage.getItem("token");

            if (!token) {
                navigate("/");
                return;
            }

            try {
                const response = await fetch("/api/contacts/" + id, {
                    headers: {
                        Authorization: "Bearer " + token,
                    },
                });

                if (!response.ok) {
                    setError("Unable to load contact");
                    return;
                }

                const data = await response.json();
                setContact(data);

                const emailResponse = await fetch(
                    "/api/contact-emails/contact/" + id,
                    {
                        headers: {
                            Authorization: "Bearer " + token,
                        },
                    }
                );

                if (emailResponse.ok) {
                    const emailData = await emailResponse.json();
                    setEmails(emailData);
                }

                const phoneResponse = await fetch(
                    "/api/contact-phones/contact/" + id,
                    {
                        headers: {
                            Authorization: "Bearer " + token,
                        },
                    }
                );

                if (phoneResponse.ok) {
                    const phoneData = await phoneResponse.json();
                    setPhones(phoneData);
                }
            } catch {
                setError("Unable to connect to the server");
            }
        }

        loadContact();
    }, [id, navigate]);

    async function handleAddEmail(event) {
        event.preventDefault();

        const token = localStorage.getItem("token");

        try {
            const response = await fetch(
                "/api/contact-emails/contact/" + id,
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                        Authorization: "Bearer " + token,
                    },
                    body: JSON.stringify({
                        emailAddress: newEmailAddress,
                        label: newEmailLabel,
                    }),
                }
            );

            if (!response.ok) {
                setError("Unable to add email address");
                return;
            }

            const newEmail = await response.json();

            setEmails([...emails, newEmail]);
            setNewEmailAddress("");
            setNewEmailLabel("");
            setError("");
        } catch {
            setError("Unable to connect to the server");
        }
    }

    async function handleDeleteEmail(emailId) {
        const token = localStorage.getItem("token");

        try {
            const response = await fetch(
                "/api/contact-emails/" + emailId,
                {
                    method: "DELETE",
                    headers: {
                        Authorization: "Bearer " + token,
                    },
                }
            );

            if (!response.ok) {
                setError("Unable to delete email address");
                return;
            }

            const updatedEmails = emails.filter(
                (email) => email.id !== emailId
            );

            setEmails(updatedEmails);
            setError("");
        } catch {
            setError("Unable to connect to the server");
        }
    }

    return (
        <div>
            <h1>Contact Details</h1>

            {error && <p>{error}</p>}

            {contact && (
                <div>
                    <h2>
                        {contact.firstName} {contact.lastName}
                    </h2>
                    <p>{contact.title}</p>

                    <h3>Email Addresses</h3>

                    <form onSubmit={handleAddEmail}>
                        <input
                            type="email"
                            placeholder="Email address"
                            value={newEmailAddress}
                            onChange={(event) => setNewEmailAddress(event.target.value)}
                            required
                        />

                        <input
                            type="text"
                            placeholder="Label"
                            value={newEmailLabel}
                            onChange={(event) => setNewEmailLabel(event.target.value)}
                            required
                        />

                        <button type="submit">Add Email</button>
                    </form>

                    {emails.length === 0 && <p>No email addresses added.</p>}

                    {emails.map((email) => (
                        <p key={email.id}>
                            {email.label}: {email.emailAddress}

                            <button
                                type="button"
                                onClick={() => handleDeleteEmail(email.id)}
                            >
                                Delete
                            </button>
                        </p>
                    ))}

                    <h3>Phone Numbers</h3>

                    {phones.length === 0 && <p>No phone numbers added.</p>}

                    {phones.map((phone) => (
                        <p key={phone.id}>
                            {phone.label}: {phone.phoneNumber}
                        </p>
                    ))} 
                </div>
            )}
        </div>
    );
}

export default ContactDetailsPage;