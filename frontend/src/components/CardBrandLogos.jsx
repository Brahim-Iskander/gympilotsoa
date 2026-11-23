/**
 * Inline SVG payment card brand logos.
 * Visa, Mastercard, Amex, Discover – used in Footer & Membership checkout.
 */

export const VisaLogo = ({ size = 36, ...props }) => (
  <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 750 471" width={size} height={size * 0.628} {...props}>
    <rect width="750" height="471" rx="40" fill="#1A1F71" />
    <path d="M278 334.2L311.6 138.4H362.6L329 334.2H278ZM524.7 142.7C514.5 138.9 498.3 134.8 478.5 134.8C428.2 134.8 392.8 161.4 392.5 199.3C392 227.6 418.2 243.3 438 252.7C458.3 262.3 465.4 268.5 465.3 277.1C465.1 290.1 449.4 296.1 434.8 296.1C414.2 296.1 403.4 293.2 386.6 285.9L379.8 282.6L372.2 330.6C384.6 336.1 407.3 340.9 430.9 341.2C484.4 341.2 519.1 315.1 519.5 274.7C519.7 252.5 506.1 235.6 477.1 221.5C458.8 212.3 447.6 206.1 447.7 196.9C447.7 188.8 457 180.1 477 180.1C493.5 179.7 505.8 183.4 515.4 187.2L520.1 189.4L527.9 142.7H524.7ZM661.6 138.4H621.4C609.2 138.4 600 141.8 594.7 154.4L522.5 334.2H575.8L586.4 305.8H651L657.3 334.2H704.5L661.6 138.4ZM601.3 268.3C605.2 258.1 621.2 214.6 621.2 214.6C620.9 215.2 625 204.5 627.3 197.9L630.5 212.8L640.4 268.3H601.3ZM232.2 138.4L182.5 271.6L177.2 245.6C168.3 216.6 141.3 185.4 111 169.7L156.3 334H210.1L286.3 138.4H232.2Z" fill="#FFFFFF" />
    <path d="M131.9 138.4H49.6L49 142.7C113.1 159.1 155.7 195.8 168.3 245.7L155.4 154.8C153.2 142.4 144.3 139 131.9 138.4Z" fill="#F9A533" />
  </svg>
);

export const MastercardLogo = ({ size = 36, ...props }) => (
  <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 750 471" width={size} height={size * 0.628} {...props}>
    <rect width="750" height="471" rx="40" fill="#16366F" />
    <circle cx="299" cy="235.5" r="140" fill="#D32011" />
    <circle cx="451" cy="235.5" r="140" fill="#F59D1C" />
    <path d="M375 125.5C403.9 149.1 423 183.8 423 223C423 262.2 403.9 296.9 375 320.5C346.1 296.9 327 262.2 327 223C327 183.8 346.1 149.1 375 125.5Z" fill="#FF5D00" />
  </svg>
);

export const AmexLogo = ({ size = 36, ...props }) => (
  <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 750 471" width={size} height={size * 0.628} {...props}>
    <rect width="750" height="471" rx="40" fill="#2E77BC" />
    <path d="M0 221H53L63 197L73 221H221V208L229 221H285V131H229V143L219 131H162L134 196L107 131H51V143L42 131H0V221ZM325 221H450V192H360V184H448V157H360V150H450V121H325V221ZM486 221H522L525 214H578L581 221H620L577 131H530L486 221ZM538 187L553 151L568 187H538ZM631 221H697L735 188V221H768V131H700L664 163V131H598L586 163L573 131H506V221H631Z" fill="white" />
    <path d="M0 250H52L70 290L89 250H142V340H105V274L83 340H57L35 274V340H0V250ZM157 340V250H234L252 274L271 250H303V340H268V282L247 340H217L196 282V340H157ZM325 340V250H405V277H360V287H403V312H360V314H405V340H325Z" fill="white" />
  </svg>
);

export const DiscoverLogo = ({ size = 36, ...props }) => (
  <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 750 471" width={size} height={size * 0.628} {...props}>
    <rect width="750" height="471" rx="40" fill="#FFFFFF" />
    <rect x="1" y="1" width="748" height="469" rx="39" fill="#FFFFFF" stroke="#D9D9D9" strokeWidth="2" />
    <path d="M0 330H750V431C750 453.1 732.1 471 710 471H40C17.9 471 0 453.1 0 431V330Z" fill="#F47216" />
    <circle cx="415" cy="235" r="80" fill="#F47216" />
    <text x="78" y="250" fontFamily="Arial,sans-serif" fontSize="80" fontWeight="bold" fill="#000">DISC</text>
    <text x="440" y="250" fontFamily="Arial,sans-serif" fontSize="80" fontWeight="bold" fill="#000">VER</text>
  </svg>
);

/**
 * Renders a horizontal row of card brand logos.
 * @param {number} size – icon height base (default 28)
 * @param {object} sx – MUI-compatible sx prop forwarded to wrapper
 */
export default function CardBrandLogos({ size = 28, sx, ...props }) {
  const logoStyle = {
    borderRadius: 4,
    display: 'block',
  };
  return (
    <span style={{ display: 'inline-flex', alignItems: 'center', gap: 6, ...sx }} {...props}>
      <VisaLogo size={size} style={logoStyle} />
      <MastercardLogo size={size} style={logoStyle} />
      <AmexLogo size={size} style={logoStyle} />
      <DiscoverLogo size={size} style={logoStyle} />
    </span>
  );
}
