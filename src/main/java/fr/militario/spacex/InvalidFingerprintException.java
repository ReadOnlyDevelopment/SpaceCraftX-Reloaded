/*
 * Library License
 *
 * Copyright (c) 2021 ReadOnly Development
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package fr.militario.spacex;

import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;

public class InvalidFingerprintException extends RuntimeException {
	private static final long serialVersionUID = -4382006867998166700L;

	private final static String header = "Invalid fingerprint detected!";
	private final static String yellAboutNoSupport = "This version will NOT be supported by the developer!";

	public InvalidFingerprintException(FMLFingerprintViolationEvent event) {
		super(header + event.getSource().getName() + " may have been tampered with. " + yellAboutNoSupport);
	}
}
